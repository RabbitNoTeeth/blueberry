package fun.bookish.blueberry.sip.command.executor.media.param;

import fun.bookish.blueberry.sip.command.executor.param.SipCommandParam;

/**
 * 媒体流点播参数
 */
public class SipMediaPlayParam extends SipCommandParam {

    private final String channelId;

    private final String ssrc;

    private final String mediaServerIp;

    private final Integer mediaServerPort;

    public SipMediaPlayParam(String deviceId, String deviceAddress, String channelId, String protocolType, String ssrc, String mediaServerIp, Integer mediaServerPort) {
        super(deviceId, deviceAddress, protocolType);
        this.channelId = channelId;
        this.ssrc = ssrc;
        this.mediaServerIp = mediaServerIp;
        this.mediaServerPort = mediaServerPort;
    }

    public String getMediaServerIp() {
        return mediaServerIp;
    }

    public Integer getMediaServerPort() {
        return mediaServerPort;
    }

    public String getSsrc() {
        return ssrc;
    }

    public String getChannelId() {
        return channelId;
    }
}
