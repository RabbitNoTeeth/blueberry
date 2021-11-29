package fun.bookish.blueberry.server.sip;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.bookish.blueberry.server.channel.entity.ChannelStatusSync;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamPO;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaStopParam;
import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceInfoParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceStatusParam;
import fun.bookish.blueberry.sip.constant.SipResultTypeEnum;
import fun.bookish.blueberry.sip.event.listener.SipEventListener;
import fun.bookish.blueberry.sip.entity.*;
import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.utils.DateUtils;
import fun.bookish.blueberry.sip.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis存储器接口实现
 */
@Component
public class MySipEventListener implements SipEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySipEventListener.class);

    private final SipServer sipServer;
    private final IDeviceService deviceService;
    private final IChannelService channelService;
    private final IVideoStreamService videoStreamService;

    @Autowired
    public MySipEventListener(SipServer sipServer,
                              IDeviceService deviceService,
                              IChannelService channelService,
                              IVideoStreamService videoStreamService) {
        this.sipServer = sipServer;
        this.deviceService = deviceService;
        this.channelService = channelService;
        this.videoStreamService = videoStreamService;
        // 将当前监听器注册到sip服务中
        sipServer.addEventListener(this);
    }

    /**
     * 设备注册
     *
     * @param sipEventContext
     */
    @Override
    public void deviceRegister(SipEventContext<SipDeviceRegisterDesc> sipEventContext) {
        try {
            SipQueryCommandExecutor sipQueryCommandExecutor = sipEventContext.getSipServer().getSipCommandExecutorManager().getSipQueryCommandExecutor();
            SipDeviceRegisterDesc deviceRegisterDesc = sipEventContext.getData();
            String requestDeviceID = deviceRegisterDesc.getFromDeviceId();
            String requestDeviceAddress = deviceRegisterDesc.getFromDeviceAddress();
            String deviceID = deviceRegisterDesc.getDeviceID();
            String transport = deviceRegisterDesc.getTransport();
            // 保存设备信息
            DevicePO deviceModel = new DevicePO();
            deviceModel.setId(deviceID);
            deviceModel.setName(deviceID);
            deviceModel.setType("GB");
            deviceModel.setOnline(1);
            deviceModel.setCommandTransport(transport);
            String[] deviceAddressSplit = requestDeviceAddress.split(":");
            deviceModel.setRemoteIp(deviceAddressSplit[0]);
            deviceModel.setRemotePort(Integer.parseInt(deviceAddressSplit[1]));
            deviceModel.setSipServerAddress(deviceRegisterDesc.getSipServerHost());
            deviceModel.setExpires(deviceRegisterDesc.getExpires());
            String now = deviceRegisterDesc.getUpdateTime();
            deviceModel.setLastRegisterAt(now);
            deviceModel.setCreatedAt(now);
            deviceModel.setUpdatedAt(now);
            deviceService.addOrUpdate(deviceModel);
            // 查询设备信息、设备状态、设备目录
            SipQueryDeviceInfoParam queryDeviceInfoParam = new SipQueryDeviceInfoParam(requestDeviceID, requestDeviceAddress, deviceID, transport);
            sipQueryCommandExecutor.queryDeviceInfo(queryDeviceInfoParam);
            SipQueryDeviceStatusParam queryDeviceStatusParam = new SipQueryDeviceStatusParam(requestDeviceID, requestDeviceAddress, deviceID, transport);
            sipQueryCommandExecutor.queryDeviceStatus(queryDeviceStatusParam);
            SipQueryCatalogParam queryCatalogParam = new SipQueryCatalogParam(requestDeviceID, requestDeviceAddress, deviceID, transport);
            sipQueryCommandExecutor.queryCatalog(queryCatalogParam);
            LOGGER.info("设备注册信息处理成功. device:{}", deviceID);
        } catch (Exception e) {
            LOGGER.error("设备注册信息处理异常", e);
        }
    }

    /**
     * 设备注销
     *
     * @param sipEventContext
     */
    @Override
    public void deviceLogout(SipEventContext<String> sipEventContext) {
        try {
            String deviceID = sipEventContext.getData();
            // 更新在线状态
            DevicePO updateDevice = new DevicePO();
            updateDevice.setOnline(0);
            updateDevice.setUpdatedAt(DateUtils.getNowDateTimeStr());
            deviceService.updateById(updateDevice);

            QueryWrapper<ChannelPO> queryWrapper = new QueryWrapper<ChannelPO>().eq("device_id", deviceID);
            ChannelPO updateChannel = new ChannelPO();
            updateChannel.setOnline("OFFLINE");
            updateChannel.setUpdatedAt(DateUtils.getNowDateTimeStr());
            channelService.update(updateChannel, queryWrapper);

            LOGGER.info("设备注销信息处理成功. device:{}", deviceID);
        } catch (Exception e) {
            LOGGER.error("设备注销信息处理异常", e);
        }
    }

    /**
     * 设备信息响应
     *
     * @param sipEventContext
     */
    @Override
    public void deviceInfoResponse(SipEventContext<SipDeviceInfoDesc> sipEventContext) {
        try {
            SipDeviceInfoDesc deviceInfoDesc = sipEventContext.getData();
            String deviceID = deviceInfoDesc.getDeviceID();
            if (SipResultTypeEnum.OK.getCode().equals(deviceInfoDesc.getResult())) {
                // 更新设备信息
                DevicePO deviceModel = new DevicePO();
                deviceModel.setId(deviceID);
                deviceModel.setName(deviceInfoDesc.getDeviceName());
                deviceModel.setManufacturer(deviceInfoDesc.getManufacturer());
                deviceModel.setModel(deviceInfoDesc.getModel());
                deviceModel.setFirmware(deviceInfoDesc.getFirmware());
                deviceModel.setUpdatedAt(deviceInfoDesc.getUpdateTime());
                deviceService.updateById(deviceModel);

                ChannelPO channelPO = new ChannelPO();
                channelPO.setId(deviceID);
                channelPO.setName(deviceInfoDesc.getDeviceName());
                channelPO.setManufacturer(deviceInfoDesc.getManufacturer());
                channelPO.setModel(deviceInfoDesc.getModel());
                channelPO.setFirmware(deviceInfoDesc.getFirmware());
                channelPO.setUpdatedAt(deviceInfoDesc.getUpdateTime());
                channelService.updateById(channelPO);
            }
            LOGGER.info("设备信息处理成功. device:{}", deviceID);
        } catch (Exception e) {
            LOGGER.error("设备信息处理异常", e);
        }
    }

    /**
     * 设备状态响应
     *
     * @param sipEventContext
     */
    @Override
    public void deviceStatusResponse(SipEventContext<SipDeviceStatusDesc> sipEventContext) {
        try {
            SipDeviceStatusDesc deviceStatusDesc = sipEventContext.getData();
            String deviceID = deviceStatusDesc.getDeviceID();
            if (SipResultTypeEnum.OK.getCode().equals(deviceStatusDesc.getResult())) {
                // 更新设备状态
                ChannelPO channelPO = new ChannelPO();
                channelPO.setId(deviceID);
                channelPO.setStatus(deviceStatusDesc.getStatus());
                channelPO.setOnline(deviceStatusDesc.getOnline());
                channelPO.setEncode(deviceStatusDesc.getEncode());
                channelPO.setRecord(deviceStatusDesc.getRecord());
                channelPO.setUpdatedAt(DateUtils.getNowDateTimeStr());
                channelService.updateById(channelPO);
            }
            LOGGER.info("设备状态处理成功. device:{}", deviceID);
        } catch (Exception e) {
            LOGGER.error("设备状态处理异常", e);
        }
    }

    /**
     * 设备目录响应
     *
     * @param sipEventContext
     */
    @Override
    public void catalogResponse(SipEventContext<SipCatalogDesc> sipEventContext) {
        try {
            SipCatalogDesc catalogDesc = sipEventContext.getData();
            String deviceID = catalogDesc.getDeviceID();
            List<SipChannelDesc> channelList = catalogDesc.getChannelList();
            if (channelList.size() > 0) {
                List<ChannelPO> channelModels = channelList.stream().map(channel -> {
                    // 更新通道信息
                    ChannelPO channelModel = new ChannelPO();
                    channelModel.setId(channel.getDeviceId());
                    channelModel.setDeviceId(deviceID);
                    channelModel.setName(channel.getName());
                    channelModel.setManufacturer(channel.getManufacturer());
                    channelModel.setModel(channel.getModel());
                    channelModel.setOwner(channel.getOwner());
                    channelModel.setCivilCode(channel.getCivilCode());
                    channelModel.setBlock(channel.getBlock());
                    channelModel.setAddress(channel.getAddress());
                    channelModel.setParental(channel.getParental());
                    channelModel.setParentId(channel.getParentId());
                    channelModel.setParentChannelId(channel.getParentChannelId());
                    channelModel.setSafetyWay(channel.getSafetyWay());
                    channelModel.setRegisterWay(channel.getRegisterWay());
                    channelModel.setCertNum(channel.getCertNum());
                    channelModel.setCertifiable(channel.getCertifiable());
                    channelModel.setErrCode(channel.getErrCode());
                    channelModel.setEndTime(channel.getEndTime());
                    channelModel.setSecrecy(channel.getSecrecy());
                    channelModel.setIpAddress(channel.getIpAddress());
                    channelModel.setPort(channel.getPort());
                    channelModel.setPassword(channel.getPassword());
                    channelModel.setStatus(channel.getStatus());
                    channelModel.setLongitude(channel.getLongitude());
                    channelModel.setLatitude(channel.getLatitude());
                    channelModel.setPtzType(channel.getPtzType());
                    channelModel.setPositionType(channel.getPositionType());
                    channelModel.setRoomType(channel.getRoomType());
                    channelModel.setUseType(channel.getUseType());
                    channelModel.setSupplyLightType(channel.getSupplyLightType());
                    channelModel.setDirectionType(channel.getDirectionType());
                    channelModel.setResolution(channel.getResolution());
                    channelModel.setBusinessGroupId(channel.getBusinessGroupId());
                    channelModel.setDownloadSpeed(channel.getDownloadSpeed());
                    channelModel.setSvcSpaceSupportMode(channel.getSvcSpaceSupportMode());
                    channelModel.setSvcTimeSupportMode(channel.getSvcTimeSupportMode());
                    String now = DateUtils.getNowDateTimeStr();
                    channelModel.setCreatedAt(now);
                    channelModel.setUpdatedAt(now);
                    return channelModel;
                }).collect(Collectors.toList());
                channelService.addOrUpdate(channelModels);
            }
            LOGGER.info("设备目录信息处理成功. device:{}, channels:{}", deviceID, channelList.size());
        } catch (Exception e) {
            LOGGER.error("设备目录息处理异常", e);
        }
    }

    @Override
    public void keepaliveNotify(SipEventContext<SipKeepaliveDesc> sipEventContext) {
        try {
            SipKeepaliveDesc keepaliveDesc = sipEventContext.getData();
            String requestDeviceID = keepaliveDesc.getFromDeviceId();
            String requestDeviceAddress = keepaliveDesc.getFromDeviceAddress();
            String deviceID = keepaliveDesc.getDeviceID();
            // 更新设备信息
            DevicePO deviceModel = new DevicePO();
            deviceModel.setId(deviceID);
            deviceModel.setType("GB");
            deviceModel.setOnline(1);
            String[] deviceAddressSplit = requestDeviceAddress.split(":");
            deviceModel.setRemoteIp(deviceAddressSplit[0]);
            deviceModel.setRemotePort(Integer.parseInt(deviceAddressSplit[1]));
            String now = keepaliveDesc.getUpdateTime();
            deviceModel.setLastKeepaliveAt(now);
            deviceModel.setUpdatedAt(now);
            deviceModel.setSipServerAddress(keepaliveDesc.getSipServerHost());
            deviceService.updateById(deviceModel);
            LOGGER.info("设备保活信息处理成功. device:{}", requestDeviceID);
        } catch (Exception e) {
            LOGGER.error("设备保活信息处理异常", e);
        }
    }

    @Override
    public void mediaInviteSuccess(SipEventContext<SipMediaInviteDesc> sipEventContext) {
        try {
            SipMediaInviteDesc mediaInviteDesc = sipEventContext.getData();
            String deviceId = mediaInviteDesc.getFromDeviceId();
            String channelId = mediaInviteDesc.getChannelId();
            String ssrc = mediaInviteDesc.getSsrc();
            VideoStreamPO mediaStreamModel = new VideoStreamPO();
            mediaStreamModel.setSsrc(ssrc);
            SipMediaStopParam stopParam = new SipMediaStopParam(deviceId, mediaInviteDesc.getFromDeviceAddress(),
                    channelId, mediaInviteDesc.getTransport(), ssrc,
                    mediaInviteDesc.getFromTag(), mediaInviteDesc.getToTag(), mediaInviteDesc.getCallId());
            mediaStreamModel.setStopParams(JsonUtils.encode(stopParam));
            videoStreamService.updateBySSRC(mediaStreamModel);
            LOGGER.info("设备视频流信息处理成功. device:{}", channelId);
        } catch (Exception e) {
            LOGGER.error("设备视频流信息处理异常", e);
        }
    }

    /**
     * 查询设备状态
     */
    public void queryDeviceStatus() {
        try {
            SipQueryCommandExecutor sipQueryCommandExecutor = sipServer.getSipCommandExecutorManager().getSipQueryCommandExecutor();
            List<ChannelStatusSync> channelStatusSyncList = channelService.queryChannelStatusSyncList();
            for (ChannelStatusSync item : channelStatusSyncList) {
                SipQueryDeviceStatusParam queryDeviceStatusParam = new SipQueryDeviceStatusParam(item.getId(), item.getAddress(), item.getDeviceId(), item.getTransport());
                sipQueryCommandExecutor.queryDeviceStatus(queryDeviceStatusParam);
            }
        } catch (Exception e) {
            LOGGER.error("设备状态查询异常", e);
        }
    }

}
