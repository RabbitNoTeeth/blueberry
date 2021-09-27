package fun.bookish.blueberry.sip.entity;


import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

/**
 * 设备媒体点播描述
 */
public class SipMediaInviteDesc extends SipDeviceDesc{

    private String channelId;
    private String ssrc;
    private String fromTag;
    private String toTag;
    private String transport;
    private String callId;

    public SipMediaInviteDesc(String requestDeviceID, String requestDeviceAddress, String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSsrc() {
        return ssrc;
    }

    public void setSsrc(String ssrc) {
        this.ssrc = ssrc;
    }

    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getCallId() {
        return this.callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
