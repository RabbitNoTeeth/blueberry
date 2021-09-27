package fun.bookish.blueberry.sip.request.handler.impl;

import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.constant.SipCommandType;
import fun.bookish.blueberry.sip.constant.SipMessageType;
import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.event.SipEventTypeEnum;
import fun.bookish.blueberry.sip.event.listener.manager.SipEventListenerManager;
import fun.bookish.blueberry.sip.exception.*;
import fun.bookish.blueberry.sip.entity.*;
import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.request.handler.AbstractSipRequestHandler;
import fun.bookish.blueberry.sip.utils.SipRequestUtils;
import fun.bookish.blueberry.sip.utils.SipTagUtils;
import fun.bookish.blueberry.sip.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MESSAGE请求处理器
 */
public class SipMessageRequestHandler extends AbstractSipRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMessageRequestHandler.class);

    @Override
    public void beforeHandle(SipRequestWrapper requestWrapper) {
        String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug(" >>> Receive a MESSAGE request. From:{}", requestDesc);
        }
    }

    @Override
    public void doHandle(SipRequestWrapper requestWrapper) {
        RequestEvent requestEvent = requestWrapper.getRequestEvent();
        Request request = requestEvent.getRequest();
        String requestDesc = SipRequestUtils.getSimpleDesc(request);
        try {
            // 解析请求content
            SAXReader reader = new SAXReader();
            reader.setEncoding("gbk");
            Document contentDoc = reader.read(new ByteArrayInputStream(request.getRawContent()));
            Element contentRootElement = contentDoc.getRootElement();
            // MESSAGE请求分为两类：来自上级（源设备）的查询、来自下级（目标设备）的响应，要做不同处理
            String messageType = contentRootElement.getName();
            // MESSAGE请求可能包含多种命令类型，需要进行判断做不同处理
            String cmdType = XmlUtils.getText(contentRootElement, "CmdType");
            if (SipMessageType.QUERY.equals(messageType)) {
                // todo P99 来自上级（源设备）的请求
            } else if (SipMessageType.RESPONSE.equals(messageType)) {
                // 来自下级（目标设备）的响应，根据命令类型做不同处理
                switch (cmdType) {
                    case SipCommandType.DEVICE_INFO:
                        // 设备信息响应
                        handleDeviceInfoResponse(requestWrapper, contentRootElement);
                        break;
                    case SipCommandType.DEVICE_STATUS:
                        // 设备状态响应
                        handleDeviceStatusResponse(requestWrapper, contentRootElement);
                        break;
                    case SipCommandType.CATALOG:
                        // 设备目录查询
                        handleCatalogResponse(requestWrapper, contentRootElement);
                        break;
                    default:
                        // 无效的消息
                        LOGGER.warn("(!) Not support the '{}:{}' type of MESSAGE request. From:{}", messageType, cmdType, requestDesc);
                        break;
                }
            } else if (SipMessageType.NOTIFY.equals(messageType)) {
                // 来自下级（目标设备）的通知，根据命令类型做不同处理
                switch (cmdType) {
                    case SipCommandType.KEEPALIVE:
                        // 设备信息报送通知
                        handleKeepaliveNotify(requestWrapper, contentRootElement);
                        break;
                    // todo P2 来自下级（目标设备）的其他类型通知
                    default:
                        // 无效的消息
//                        LOGGER.warn("(!) Not support the '{}:{}' type of MESSAGE request. From:{}", messageType, cmdType, requestDesc);
                        break;
                }
            } else {
                // 无效的消息
//                LOGGER.warn("暂不支持当前类型MESSAGE请求处理, type={}, cmd={}, {}", messageType, cmdType, from);
            }
        } catch (Exception e) {
            LOGGER.error("(×) Failed to handle the MESSAGE request. From:{}", requestDesc, e);
        }
    }

    @Override
    public void afterHandle(SipRequestWrapper requestWrapper) {
        String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
        SipDeviceDesc deviceDesc = (SipDeviceDesc) requestWrapper.getData();
        if (deviceDesc != null) {
            SipEventListenerManager listenerManager = requestWrapper.getSipServer().getSipEventListenerManager();
            SipEventTypeEnum eventType = deviceDesc.getEventType();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fire the sip event '{}'. Device:{}", eventType, requestDesc);
            }
            // 触发sip事件
            listenerManager.fire(new SipEventContext<>(eventType, requestWrapper.getSipServer(), deviceDesc));
        }
    }

    /**
     * 处理设备信息响应（来自下级【目的设备】）
     *
     * @param requestWrapper
     * @param contentRootElement
     */
    private void handleDeviceInfoResponse(SipRequestWrapper requestWrapper, Element contentRootElement) throws SipDeviceInfoResponseCreationException {
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDesc = SipRequestUtils.getSimpleDesc(request);
        try {
            LOGGER.info(">>> Receive DeviceInfo Response. From:{}", requestDesc);
            MessageFactory messageFactory = requestWrapper.getSipServer().getSipFactory().createMessageFactory();
            ServerTransaction serverTransaction = getServerTransaction(requestWrapper);
            // 返回ok响应
            Response response = messageFactory.createResponse(Response.OK, request);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(SipTagUtils.random());
            serverTransaction.sendResponse(response);
            LOGGER.info("<<< Response the DeviceInfo Response. To:{}", requestDesc);
            // 设置数据到请求中，afterHandler中发送到各事件监听器
            requestWrapper.setData(createSipDeviceInfoDesc(requestWrapper, contentRootElement));
        } catch (Exception e) {
            throw new SipDeviceInfoResponseCreationException("(×) Failed to response the DeviceInfo Response. To:" + requestDesc, e);
        }
    }

    /**
     * 创建设备信息描述
     *
     * @param requestWrapper
     * @param contentRootElement
     * @return
     */
    private SipDeviceInfoDesc createSipDeviceInfoDesc(SipRequestWrapper requestWrapper, Element contentRootElement) {
        // 解析设备信息数据
        String deviceId = XmlUtils.getText(contentRootElement, "DeviceID");
        String deviceName = XmlUtils.getText(contentRootElement, "DeviceName");
        String result = XmlUtils.getText(contentRootElement, "Result");
        String manufacturer = XmlUtils.getText(contentRootElement, "Manufacturer");
        String model = XmlUtils.getText(contentRootElement, "Model");
        String firmware = XmlUtils.getText(contentRootElement, "Firmware");
        String channel = XmlUtils.getText(contentRootElement, "Channel");
        List<Element> info = contentRootElement.elements("Info");
        List<String> infoList = info == null ? null : info.stream().map(Element::getText).collect(Collectors.toList());

        // 获取请求来源设备id及地址
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDeviceId = SipRequestUtils.getFromDeviceId(request);
        String requestDeviceAddress = SipRequestUtils.getFromDeviceAddress(request);
        String sipServerAddress = SipRequestUtils.getSipServerAddress(requestWrapper);

        // 创建设备信息描述
        SipDeviceInfoDesc deviceInfoDesc = new SipDeviceInfoDesc(requestDeviceId, requestDeviceAddress, deviceId, sipServerAddress, SipEventTypeEnum.DEVICE_INFO_RESPONSE);
        deviceInfoDesc.setDeviceName(deviceName);
        deviceInfoDesc.setResult(result);
        deviceInfoDesc.setManufacturer(manufacturer);
        deviceInfoDesc.setModel(model);
        deviceInfoDesc.setFirmware(firmware);
        deviceInfoDesc.setChannel(StringUtils.isBlank(channel) ? null : Integer.parseInt(channel));
        deviceInfoDesc.setInfo(infoList);
        return deviceInfoDesc;
    }

    /**
     * 处理设备状态响应（来自下级【目的设备】）
     *
     * @param requestWrapper
     * @param contentRootElement
     */
    private void handleDeviceStatusResponse(SipRequestWrapper requestWrapper, Element contentRootElement) throws SipDeviceStatusResponseCreationException {
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDesc = SipRequestUtils.getSimpleDesc(request);
        try {
            LOGGER.info(">>> Receive DeviceStatus Response. From:{}", requestDesc);
            MessageFactory messageFactory = requestWrapper.getSipServer().getSipFactory().createMessageFactory();
            ServerTransaction serverTransaction = getServerTransaction(requestWrapper);
            // 返回ok响应
            Response response = messageFactory.createResponse(Response.OK, request);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(SipTagUtils.random());
            serverTransaction.sendResponse(response);
            LOGGER.info("<<< Response the DeviceStatus Response. To:{}", requestDesc);
            // 设置数据到请求中，afterHandler中发送到各事件监听器
            requestWrapper.setData(createSipDeviceStatusDesc(requestWrapper, contentRootElement));
        } catch (Exception e) {
            throw new SipDeviceStatusResponseCreationException("(×) Failed to response the DeviceStatus Response. To:" + requestDesc, e);
        }
    }

    /**
     * 创建设备状态描述
     *
     * @param requestWrapper
     * @param contentRootElement
     * @return
     */
    private SipDeviceStatusDesc createSipDeviceStatusDesc(SipRequestWrapper requestWrapper, Element contentRootElement) {
        // 解析设备状态数据
        String deviceId = XmlUtils.getText(contentRootElement, "DeviceID");
        String result = XmlUtils.getText(contentRootElement, "Result");
        String online = XmlUtils.getText(contentRootElement, "Online");
        String status = XmlUtils.getText(contentRootElement, "Status");
        String reason = XmlUtils.getText(contentRootElement, "Reason");
        String encode = XmlUtils.getText(contentRootElement, "Encode");
        String record = XmlUtils.getText(contentRootElement, "Record");
        String deviceTime = XmlUtils.getText(contentRootElement, "DeviceTime");
        Element alarmStatus = contentRootElement.element("Alarmstatus");
        List<SipDeviceAlarmStatusDesc> alarmStatusList = null;
        if (alarmStatus != null) {
            alarmStatusList = new ArrayList<>();
            Iterator<Element> iterator = alarmStatus.elementIterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Element e = iterator.next();
                    String deviceID = XmlUtils.getText(e, "DeviceID");
                    String dutyStatus = XmlUtils.getText(e, "DutyStatus");
                    alarmStatusList.add(new SipDeviceAlarmStatusDesc(deviceID, dutyStatus));
                }
            }
        }
        List<Element> info = contentRootElement.elements("Info");
        List<String> infoList = info == null ? null : info.stream().map(Element::getText).collect(Collectors.toList());

        // 获取请求来源设备id及地址
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDeviceId = SipRequestUtils.getFromDeviceId(request);
        String requestDeviceAddress = SipRequestUtils.getFromDeviceAddress(request);
        String sipServerAddress = SipRequestUtils.getSipServerAddress(requestWrapper);

        // 创建设备状态描述
        SipDeviceStatusDesc sipDeviceStatusDesc = new SipDeviceStatusDesc(requestDeviceId, requestDeviceAddress, deviceId, sipServerAddress, SipEventTypeEnum.DEVICE_STATUS_RESPONSE);
        sipDeviceStatusDesc.setResult(result);
        sipDeviceStatusDesc.setOnline(online);
        sipDeviceStatusDesc.setReason(reason);
        sipDeviceStatusDesc.setStatus(status);
        sipDeviceStatusDesc.setEncode(encode);
        sipDeviceStatusDesc.setRecord(record);
        sipDeviceStatusDesc.setDeviceTime(deviceTime);
        sipDeviceStatusDesc.setAlarmStatus(alarmStatusList);
        sipDeviceStatusDesc.setInfo(infoList);
        return sipDeviceStatusDesc;
    }

    /**
     * 处理设备目录响应（来自下级【目的设备】）
     *
     * @param requestWrapper
     * @param contentRootElement
     */
    private void handleCatalogResponse(SipRequestWrapper requestWrapper, Element contentRootElement) throws SipCatalogResponseCreationException {
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDesc = SipRequestUtils.getSimpleDesc(request);
        try {
            LOGGER.info(">>> Receive Catalog Response. From:{}", requestDesc);
            MessageFactory messageFactory = requestWrapper.getSipServer().getSipFactory().createMessageFactory();
            ServerTransaction serverTransaction = getServerTransaction(requestWrapper);
            // 返回ok响应
            Response response = messageFactory.createResponse(Response.OK, request);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(SipTagUtils.random());
            // 设置数据到请求中，afterHandler中发送到各事件监听器
            requestWrapper.setData(createSipCatalogDesc(requestWrapper, contentRootElement));
            serverTransaction.sendResponse(response);
            LOGGER.info("<<< Response the Catalog Response. To:{}", requestDesc);
        } catch (Exception e) {
            throw new SipCatalogResponseCreationException("(×) Failed to response the Catalog Response. To:" + requestDesc, e);
        }
    }

    /**
     * 创建设备目录描述
     *
     * @param requestWrapper
     * @param contentRootElement
     * @return
     */
    private SipCatalogDesc createSipCatalogDesc(SipRequestWrapper requestWrapper, Element contentRootElement) throws SipCommandExecuteException {
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDeviceId = SipRequestUtils.getFromDeviceId(request);
        String requestDeviceAddress = SipRequestUtils.getFromDeviceAddress(request);
        String transport = SipRequestUtils.getTransport(request);
        SipQueryCommandExecutor sipQueryCommandExecutor = requestWrapper.getSipServer().getSipCommandExecutorManager().getSipQueryCommandExecutor();
        // 解析设备目录数据
        String deviceID = XmlUtils.getText(contentRootElement, "DeviceID");
        Element deviceList = contentRootElement.element("DeviceList");
        List<SipChannelDesc> deviceItemDescList = null;
        if (deviceList != null) {
            deviceItemDescList = new ArrayList<>();
            Iterator<Element> iterator = deviceList.elementIterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Element e = iterator.next();
                    String subDeviceID = XmlUtils.getText(e, "DeviceID");
                    String name = XmlUtils.getText(e, "Name");
                    String manufacturer = XmlUtils.getText(e, "Manufacturer");
                    String model = XmlUtils.getText(e, "Model");
                    String owner = XmlUtils.getText(e, "Owner");
                    String civilCode = XmlUtils.getText(e, "CivilCode");
                    String block = XmlUtils.getText(e, "Block");
                    String address = XmlUtils.getText(e, "Address");
                    String parental = XmlUtils.getText(e, "Parental");
                    String parentId = XmlUtils.getText(e, "ParentID");
                    String safetyWay = XmlUtils.getText(e, "SafetyWay");
                    String registerWay = XmlUtils.getText(e, "RegisterWay");
                    String certNum = XmlUtils.getText(e, "CertNum");
                    String certifiable = XmlUtils.getText(e, "Certifiable");
                    String errCode = XmlUtils.getText(e, "ErrCode");
                    String endTime = XmlUtils.getText(e, "EndTime");
                    String secrecy = XmlUtils.getText(e, "Secrecy");
                    String ipAddress = XmlUtils.getText(e, "IPAddress");
                    String port = XmlUtils.getText(e, "Port");
                    String password = XmlUtils.getText(e, "Password");
                    String status = XmlUtils.getText(e, "Status");
                    String longitude = XmlUtils.getText(e, "Longitude");
                    String latitude = XmlUtils.getText(e, "Latitude");
                    String pTZType = XmlUtils.getText(e, "PTZType");
                    String positionType = XmlUtils.getText(e, "PositionType");
                    String roomType = XmlUtils.getText(e, "RoomType");
                    String useType = XmlUtils.getText(e, "UseType");
                    String supplyLightType = XmlUtils.getText(e, "SupplyLightType");
                    String directionType = XmlUtils.getText(e, "DirectionType");
                    String resolution = XmlUtils.getText(e, "Resolution");
                    String businessGroupID = XmlUtils.getText(e, "BusinessGroupID");
                    String downloadSpeed = XmlUtils.getText(e, "DownloadSpeed");
                    String svcSpaceSupportMode = XmlUtils.getText(e, "SVCSpaceSupportMode");
                    String svcTimeSupportMode = XmlUtils.getText(e, "SVCTimeSupportMode");
                    // 创建设备描述
                    SipChannelDesc deviceItemDesc = new SipChannelDesc();
                    deviceItemDesc.setDeviceId(subDeviceID);
                    deviceItemDesc.setName(name);
                    deviceItemDesc.setManufacturer(manufacturer);
                    deviceItemDesc.setModel(model);
                    deviceItemDesc.setOwner(owner);
                    deviceItemDesc.setCivilCode(civilCode);
                    deviceItemDesc.setBlock(block);
                    deviceItemDesc.setAddress(address);
                    deviceItemDesc.setParental(StringUtils.isNotBlank(parental) ? Integer.parseInt(parental) : null);
                    deviceItemDesc.setParentId(parentId);
                    // 新增父层级ID，因为部分sip平台作为下级接入时，其parentID不一定与层级关系一致
                    if (!subDeviceID.equals(deviceID)) {
                        deviceItemDesc.setParentChannelId(deviceID);
                    }
                    deviceItemDesc.setSafetyWay(StringUtils.isNotBlank(safetyWay) ? Integer.parseInt(safetyWay) : null);
                    deviceItemDesc.setRegisterWay(StringUtils.isNotBlank(registerWay) ? Integer.parseInt(registerWay) : null);
                    deviceItemDesc.setCertNum(certNum);
                    deviceItemDesc.setCertifiable(StringUtils.isNotBlank(certifiable) ? Integer.parseInt(certifiable) : null);
                    deviceItemDesc.setErrCode(StringUtils.isNotBlank(errCode) ? Integer.parseInt(errCode) : null);
                    deviceItemDesc.setEndTime(endTime);
                    deviceItemDesc.setSecrecy(StringUtils.isNotBlank(secrecy) ? Integer.parseInt(secrecy) : null);
                    deviceItemDesc.setIpAddress(ipAddress);
                    deviceItemDesc.setPort(StringUtils.isNotBlank(port) ? Integer.parseInt(port) : null);
                    deviceItemDesc.setPassword(password);
                    deviceItemDesc.setStatus(status);
                    deviceItemDesc.setLongitude(StringUtils.isNotBlank(longitude) ? Double.parseDouble(longitude) : null);
                    deviceItemDesc.setLatitude(StringUtils.isNotBlank(latitude) ? Double.parseDouble(latitude) : null);
                    deviceItemDesc.setPtzType(StringUtils.isNotBlank(pTZType) ? Integer.parseInt(pTZType) : null);
                    deviceItemDesc.setPositionType(StringUtils.isNotBlank(positionType) ? Integer.parseInt(positionType) : null);
                    deviceItemDesc.setRoomType(StringUtils.isNotBlank(roomType) ? Integer.parseInt(roomType) : null);
                    deviceItemDesc.setUseType(StringUtils.isNotBlank(useType) ? Integer.parseInt(useType) : null);
                    deviceItemDesc.setSupplyLightType(StringUtils.isNotBlank(supplyLightType) ? Integer.parseInt(supplyLightType) : null);
                    deviceItemDesc.setDirectionType(StringUtils.isNotBlank(directionType) ? Integer.parseInt(directionType) : null);
                    deviceItemDesc.setResolution(resolution);
                    deviceItemDesc.setBusinessGroupId(businessGroupID);
                    deviceItemDesc.setDownloadSpeed(downloadSpeed);
                    deviceItemDesc.setSvcSpaceSupportMode(StringUtils.isNotBlank(svcSpaceSupportMode) ? Integer.parseInt(svcSpaceSupportMode) : null);
                    deviceItemDesc.setSvcTimeSupportMode(StringUtils.isNotBlank(svcTimeSupportMode) ? Integer.parseInt(svcTimeSupportMode) : null);
                    // 添加到列表
                    deviceItemDescList.add(deviceItemDesc);

                    // 由于在与其他平台对接时发现，部分平台的设备目录没有严格按照层级结构返回，可能导致此处无限查询问题，所以暂时关闭自动查询下一层级功能，需要客户端自己实现
//                    // 判断是否有子设备
//                    if ("1".equals(parental)) {
//                        // 请求子设备的设备目录
//                        sipQueryCommandExecutor.queryCatalog(new SipQueryCatalogParam(requestDeviceId, requestDeviceAddress, subDeviceID, transport));
//                    }
                }
            }
        }

        // 创建设备目录描述
        String registerServer = SipRequestUtils.getSipServerAddress(requestWrapper);
        SipCatalogDesc sipCatalogDesc = new SipCatalogDesc(requestDeviceId, requestDeviceAddress, deviceID, registerServer, SipEventTypeEnum.CATALOG_RESPONSE);
        sipCatalogDesc.setChannelList(deviceItemDescList);

        return sipCatalogDesc;
    }

    /**
     * 处理设备信息报送通知
     *
     * @param requestWrapper
     * @param contentRootElement
     */
    private void handleKeepaliveNotify(SipRequestWrapper requestWrapper, Element contentRootElement) throws SipKeepaliveResponseCreationException {
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDesc = SipRequestUtils.getSimpleDesc(request);
        try {
            LOGGER.info(">>> Receive a keepalive notify request. From:{}", requestDesc);
            MessageFactory messageFactory = requestWrapper.getSipServer().getSipFactory().createMessageFactory();
            ServerTransaction serverTransaction = getServerTransaction(requestWrapper);
            // 返回ok响应
            Response response = messageFactory.createResponse(Response.OK, request);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(SipTagUtils.random());
            serverTransaction.sendResponse(response);
            LOGGER.info("<<< Response 'OK' to the keepalive notify request. To:{}", requestDesc);
            // 设置数据到请求中，afterHandler中发送到各事件监听器
            requestWrapper.setData(createSipKeepaliveDesc(requestWrapper, contentRootElement));
        } catch (Exception e) {
            throw new SipKeepaliveResponseCreationException("(×) Failed to response the keepalive notify request. To:" + requestDesc, e);
        }
    }

    /**
     * 创建设备状态信息推送描述
     *
     * @param requestWrapper
     * @param contentRootElement
     * @return
     */
    private SipDeviceDesc createSipKeepaliveDesc(SipRequestWrapper requestWrapper, Element contentRootElement) {
        // 解析设备状态数据
        String deviceId = XmlUtils.getText(contentRootElement, "DeviceID");
        String status = XmlUtils.getText(contentRootElement, "Status");
        List<Element> info = contentRootElement.elements("Info");
        List<String> infoList = info == null ? null : info.stream().map(Element::getText).collect(Collectors.toList());

        // 获取请求来源设备id及地址
        Request request = requestWrapper.getRequestEvent().getRequest();
        String requestDeviceId = SipRequestUtils.getFromDeviceId(request);
        String requestDeviceAddress = SipRequestUtils.getFromDeviceAddress(request);
        String sipServerAddress = SipRequestUtils.getSipServerAddress(requestWrapper);

        // 创建设备信息描述
        SipKeepaliveDesc sipDeviceStatusDesc = new SipKeepaliveDesc(requestDeviceId, requestDeviceAddress, deviceId, sipServerAddress, SipEventTypeEnum.KEEPALIVE_NOTIFY);
        sipDeviceStatusDesc.setStatus(status);
        sipDeviceStatusDesc.setInfo(infoList);
        return sipDeviceStatusDesc;
    }

}
