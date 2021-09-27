package fun.bookish.blueberry.sip.command.executor.query.impl;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.query.AbstractSipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceInfoParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceStatusParam;
import fun.bookish.blueberry.sip.exception.SipCommandExecuteException;
import fun.bookish.blueberry.sip.request.producer.SipRequestProducer;
import fun.bookish.blueberry.sip.utils.SipSNUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;

/**
 * sip查询命令执行器实现
 */
public class SipQueryCommandExecutorImpl extends AbstractSipQueryCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipQueryCommandExecutorImpl.class);

    public SipQueryCommandExecutorImpl(SipServer sipServer) {
        super(sipServer);
    }

    /**
     * 查询设备信息
     *
     * @param param
     */
    @Override
    public void queryDeviceInfo(SipQueryDeviceInfoParam param) throws SipCommandExecuteException {
        String toDeviceId = param.getDeviceId();
        String toAddress = param.getDeviceAddress();
        String deviceId = param.getQueryDeviceId();
        String transport = param.getTransport();
        String to = "<" + toDeviceId + "@" + deviceId + ">";
        try {
            SipRequestProducer sipRequestProducer = sipServer.getSipRequestProducer();
            // 组装请求内容
            String content =
                    "<?xml version=\"1.0\" encoding=\"GB2312\"?>" +
                    "<Query>" +
                    "<CmdType>DeviceInfo</CmdType>" +
                    "<SN>" + SipSNUtils.generateSN() + "</SN>" +
                    "<DeviceID>" + deviceId + "</DeviceID>" +
                    "</Query>";
            // 创建sip请求
            Request request = sipRequestProducer.createMessageRequest(toDeviceId, toAddress, deviceId, transport, content);
            // 发送sip请求
            ClientTransaction clientTransaction = getClientTransaction(transport, request);
            clientTransaction.sendRequest();
            LOGGER.info("<<< Succeed in sending a MESSAGE request of 'Query-DeviceInfo' . To:{}", to);
        } catch (Exception e) {
            throw new SipCommandExecuteException("(×) Failed to send a MESSAGE request of 'Query-DeviceInfo' . To:" + to, e);
        }
    }

    /**
     * 查询设备状态
     *
     * @param param
     */
    @Override
    public void queryDeviceStatus(SipQueryDeviceStatusParam param) throws SipCommandExecuteException {
        String toDeviceId = param.getDeviceId();
        String toAddress = param.getDeviceAddress();
        String deviceId = param.getDeviceId();
        String transport = param.getTransport();
        String to = "<" + toDeviceId + "@" + deviceId + ">";
        try {
            SipRequestProducer sipRequestProducer = sipServer.getSipRequestProducer();
            // 组装请求内容
            String content =
                    "<?xml version=\"1.0\" encoding=\"GB2312\"?>" +
                    "<Query>" +
                    "<CmdType>DeviceStatus</CmdType>" +
                    "<SN>" + SipSNUtils.generateSN() + "</SN>" +
                    "<DeviceID>" + deviceId + "</DeviceID>" +
                    "</Query>";
            // 创建sip请求
            Request request = sipRequestProducer.createMessageRequest(toDeviceId, toAddress, deviceId, transport, content);
            // 发送sip请求
            ClientTransaction clientTransaction = getClientTransaction(transport, request);
            clientTransaction.sendRequest();
            LOGGER.info("<<< Succeed in sending a MESSAGE request of 'Query-DeviceStatus' . To:{}", to);
        } catch (Exception e) {
            throw new SipCommandExecuteException("(×) Failed to send a MESSAGE request of 'Query-DeviceStatus' . To:" + to, e);
        }
    }

    @Override
    public void queryCatalog(SipQueryCatalogParam param) throws SipCommandExecuteException {
        String toDeviceId = param.getDeviceId();
        String toAddress = param.getDeviceAddress();
        String deviceId = param.getDeviceId();
        String transport = param.getTransport();
        String to = "<" + toDeviceId + "@" + deviceId + ">";
        try {
            SipRequestProducer sipRequestProducer = sipServer.getSipRequestProducer();
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            // 组装请求内容
            String content =
                    "<?xml version=\"1.0\" encoding=\"GB2312\"?>" +
                            "<Query>" +
                            "<CmdType>Catalog</CmdType>" +
                            "<SN>" + SipSNUtils.generateSN() + "</SN>" +
                            "<DeviceID>" + deviceId + "</DeviceID>" +
                            (StringUtils.isBlank(startTime) ? "" : ("<StartTime>" + startTime + "</StartTime>")) +
                            (StringUtils.isBlank(endTime) ? "" : ("<EndTime>" + endTime + "</EndTime>")) +
                            "</Query>";
            // 创建sip请求
            Request request = sipRequestProducer.createMessageRequest(toDeviceId, toAddress, deviceId, transport, content);
            // 发送sip请求
            ClientTransaction clientTransaction = getClientTransaction(transport, request);
            clientTransaction.sendRequest();
            LOGGER.info("<<< Succeed in sending a MESSAGE request of 'Query-Catalog' . To:{}", to);
        } catch (Exception e) {
            throw new SipCommandExecuteException("(×) Failed to send a MESSAGE request of 'Query-Catalog' . To:" + to, e);
        }
    }

}
