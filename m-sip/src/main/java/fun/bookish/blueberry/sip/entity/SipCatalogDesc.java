package fun.bookish.blueberry.sip.entity;

import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

import java.util.List;

/**
 * catalog描述
 */
public class SipCatalogDesc extends SipDeviceDesc{

    private List<SipChannelDesc> channelList;

    public SipCatalogDesc(String requestDeviceID, String requestDeviceAddress,String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }

    public List<SipChannelDesc> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<SipChannelDesc> channelList) {
        this.channelList = channelList;
    }
}
