package fun.bookish.blueberry.sip.entity;

import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

/**
 * 设备注册回调数据
 */
public class SipDeviceRegisterDesc extends SipDeviceDesc{

    /**
     * 设备注册地址
     */
    private String registerAddress;
    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport;
    /**
     * 设备过期时长
     */
    private int expires;

    public SipDeviceRegisterDesc(String requestDeviceID, String requestDeviceAddress,String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

}
