package fun.bookish.blueberry.sip.response.handler.impl;

import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.event.SipEventTypeEnum;
import fun.bookish.blueberry.sip.event.listener.manager.SipEventListenerManager;
import fun.bookish.blueberry.sip.entity.SipMediaInviteDesc;
import fun.bookish.blueberry.sip.response.handler.AbstractSipResponseHandler;
import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import fun.bookish.blueberry.sip.ssrc.SipSSRCManager;
import fun.bookish.blueberry.sip.utils.SipResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.Dialog;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * INVITE响应处理器
 */
public class SipInviteResponseHandler extends AbstractSipResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipInviteResponseHandler.class);

    private final Pattern ssrc_pattern = Pattern.compile("[.\\s\\S]*y=(\\d{10})[.\\s\\S]*");

    @Override
    public void beforeHandle(SipResponseWrapper responseWrapper) {
    }

    @Override
    public void doHandle(SipResponseWrapper responseWrapper) {
        SipSSRCManager sipSSRCManager = responseWrapper.getSipServer().getSipSSRCManager();
        String requestDesc = SipResponseUtils.getSimpleDesc(responseWrapper.getResponseEvent().getResponse());
        try {
            ResponseEvent responseEvent = responseWrapper.getResponseEvent();
            Response response = responseEvent.getResponse();
            int statusCode = response.getStatusCode();
            LOGGER.info(">>> Receive an INVITE response with status '{}'. From:{}", statusCode, requestDesc);
            if (statusCode == Response.TRYING) {
                // do nothing
            } else if (statusCode == Response.OK) {
                if (response.getRawContent() != null) {
                    responseWrapper.setData(new String(response.getRawContent()));
                }
                Dialog dialog = responseEvent.getDialog();
                CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
                Request reqAck = dialog.createAck(cseq.getSeqNumber());
                dialog.sendAck(reqAck);
                LOGGER.info("<<< Response 'ACK' to the INVITE response with status 'OK'. To:{}", requestDesc);
            } else {
                // 其他响应视为点播失败
                FromHeader fromHeader = (FromHeader) response.getHeader(FromHeader.NAME);
                // 通过fromTag释放ssrc
                sipSSRCManager.releaseSSRCByFromTag(fromHeader.getTag());
            }
        } catch (Exception e) {
            LOGGER.error("(×) Failed to handle the INVITE response. From:{}", requestDesc, e);
        }
    }

    @Override
    public void afterHandle(SipResponseWrapper responseWrapper) {
        int statusCode = responseWrapper.getResponseEvent().getResponse().getStatusCode();
        if (statusCode != Response.OK) {
            return;
        }
        SipEventTypeEnum eventType = SipEventTypeEnum.MEDIA_INVITE_SUCCESS;
        Response response = responseWrapper.getResponseEvent().getResponse();
        String responseDesc = SipResponseUtils.getSimpleDesc(response);
        String fromDeviceId = SipResponseUtils.getFromDeviceId(response);
        String fromAddress = SipResponseUtils.getFromAddress(response);
        String transport = SipResponseUtils.getTransport(response);
        String sipServerAddress = SipResponseUtils.getSipServerAddress(responseWrapper);
        try {
            String channelId = SipResponseUtils.getChannelId(response);
            Object content = responseWrapper.getData();
            String ssrc = parseSSRC(content == null ? null : content.toString());
            FromHeader fromHeader = (FromHeader) response.getHeader(FromHeader.NAME);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            CallIdHeader callIdHeader = (CallIdHeader) response.getHeader(CallIdHeader.NAME);
            // 创建媒体点播描述
            SipMediaInviteDesc mediaInviteDesc = new SipMediaInviteDesc(fromDeviceId, fromAddress, channelId, sipServerAddress, SipEventTypeEnum.MEDIA_INVITE_SUCCESS);
            mediaInviteDesc.setChannelId(channelId);
            mediaInviteDesc.setSsrc(ssrc);
            mediaInviteDesc.setFromTag(fromHeader.getTag());
            mediaInviteDesc.setToTag(toHeader.getTag());
            mediaInviteDesc.setTransport(transport);
            mediaInviteDesc.setCallId(callIdHeader.getCallId());
            // 触发事件监听
            SipEventListenerManager listenerManager = responseWrapper.getSipServer().getSipEventListenerManager();
            listenerManager.fire(new SipEventContext<>(eventType, responseWrapper.getSipServer(), mediaInviteDesc));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fire the sip event '{}'. Device:{}", eventType, responseDesc);
            }
        } catch (Exception e) {
            LOGGER.error("(×) Failed to fire sip event '{}'. Device:{}", eventType, responseDesc, e);
        }
    }

    /**
     * 解析ssrc
     *
     * @param content
     * @return
     */
    private String parseSSRC(String content) throws SipException {
        if (StringUtils.isNotBlank(content)) {
            Matcher matcher = ssrc_pattern.matcher(content);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        throw new SipException("there was no ssrc in the INVITE response");
    }

}
