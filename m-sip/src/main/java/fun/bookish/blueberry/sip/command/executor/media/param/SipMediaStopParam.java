package fun.bookish.blueberry.sip.command.executor.media.param;

import fun.bookish.blueberry.sip.command.executor.param.SipCommandParam;

/**
 * 媒体流点播参数
 */
public class SipMediaStopParam extends SipCommandParam {

    private final String channelId;

    private final String ssrc;

    private final String fromTag;

    private final String toTag;

    private final String callId;

    public SipMediaStopParam(String deviceId, String deviceAddress, String channelId, String protocolType, String ssrc, String fromTag, String toTag, String callId) {
        super(deviceId, deviceAddress, protocolType);
        this.channelId = channelId;
        this.ssrc = ssrc;
        this.fromTag = fromTag;
        this.toTag = toTag;
        this.callId = callId;
    }

    public String getSsrc() {
        return ssrc;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getFromTag() {
        return fromTag;
    }

    public String getToTag() {
        return toTag;
    }

    public String getCallId() {
        return callId;
    }
}
