package fun.bookish.blueberry.sip.command.executor.media.impl;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.media.AbstractSipMediaCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaPlayParam;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaStopParam;
import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.exception.SipCommandExecuteException;
import fun.bookish.blueberry.sip.request.producer.SipRequestProducer;
import fun.bookish.blueberry.sip.ssrc.SipSSRCManager;
import fun.bookish.blueberry.sip.utils.SipTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.address.SipURI;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sip媒体流命令执行器实现
 */
public class SipMediaCommandExecutorImpl extends AbstractSipMediaCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMediaCommandExecutorImpl.class);

    public SipMediaCommandExecutorImpl(SipServer sipServer) {
        super(sipServer);
    }

    /**
     * 点播实时媒体流
     */
    @Override
    public void playStart(SipMediaPlayParam param) throws SipCommandExecuteException {
        SipServerConf sipServerConf = sipServer.getSipServerConf();
        SipSSRCManager sipSSRCManager = sipServer.getSipSSRCManager();
        String deviceId = param.getDeviceId();
        String deviceAddress = param.getDeviceAddress();
        String channelId = param.getChannelId();
        String transport = param.getTransport();
        String ssrc = param.getSsrc();
        String mediaServerIp = param.getMediaServerIp();
        Integer mediaServerPort = param.getMediaServerPort();
        String to = "<" + deviceId + "@" + channelId + ">";
        try {
            SipRequestProducer sipRequestProducer = sipServer.getSipRequestProducer();
            String lineSeparator = System.lineSeparator();

            // 组装请求内容
            String content =
                    "v=0" + lineSeparator +
                    "o=" + channelId + " 0 0 IN IP4 " + sipServerConf.getHost() + lineSeparator +
                    "s=Play" + lineSeparator +
                    "c=IN IP4 " + mediaServerIp + lineSeparator +
                    "t=0 0" + lineSeparator +
                    "m=video " + mediaServerPort + ("TCP".equals(transport) ? " TCP/RTP/AVP " : " RTP/AVP ") + "96 98 97" + lineSeparator +
                    "a=recvonly" + lineSeparator +
                    "a=rtpmap:96 PS/90000" + lineSeparator +
                    "a=rtpmap:98 H264/90000" + lineSeparator +
                    "a=rtpmap:97 MPEG4/90000" + lineSeparator +
                    ("TCP".equals(transport) ? ("a=setup:passive" + lineSeparator + "a=connection:new" + lineSeparator) : "") +
                    "y=" + ssrc + lineSeparator;
            // 创建sip请求
            String fromTag = SipTagUtils.random();
            Request request = sipRequestProducer.createInviteRequest(deviceId, deviceAddress, channelId, transport, fromTag, content);
            // 发送sip请求
            ClientTransaction clientTransaction = getClientTransaction(transport, request);
            clientTransaction.sendRequest();
            // 保存ssrc
            sipSSRCManager.addFromTag(ssrc, fromTag);
            LOGGER.info("<<< Succeed in sending an INVITE request of 'Play' . To:{}", to);
        } catch (Exception e) {
            throw new SipCommandExecuteException("(×) Failed to send an INVITE request of 'Play' . To:" + to, e);
        }
    }

    @Override
    public void playStop(SipMediaStopParam param) throws SipCommandExecuteException {
        SipSSRCManager sipSSRCManager = sipServer.getSipSSRCManager();
        String deviceId = param.getDeviceId();
        String deviceAddress = param.getDeviceAddress();
        String channelId = param.getDeviceId();
        String ssrc = param.getSsrc();
        String transport = param.getTransport();
        String fromTag = param.getFromTag();
        String toTag = param.getToTag();
        String callId = param.getCallId();
        String to = "<" + deviceId + "@" + channelId + ">";
        try {
            SipRequestProducer sipRequestProducer = sipServer.getSipRequestProducer();
            Request request = sipRequestProducer.createByeRequest(deviceId, deviceAddress, channelId, transport, fromTag, toTag, callId);
            // 发送sip请求
            ClientTransaction clientTransaction = getClientTransaction(transport, request);
            Dialog dialog = clientTransaction.getDialog();
            if (dialog != null) {
                dialog.sendRequest(clientTransaction);
            } else {
                clientTransaction.sendRequest();
            }
            // 释放ssrc
            sipSSRCManager.releaseSSRC(ssrc);
            LOGGER.info("<<< Succeed in sending a BYE request. To:{}", to);
        } catch (Exception e) {
            throw new SipCommandExecuteException("(×) Failed to send a BYE request. To:" + to, e);
        }
    }

}
