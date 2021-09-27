package fun.bookish.blueberry.sip.entity;


import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

import java.util.List;

/**
 * 设备状态信息报送描述
 */
public class SipKeepaliveDesc extends SipDeviceDesc{

    /**
     * 是否正常工作(必选)：
     *      OK
     *      ERROR
     */
    private String status;
    /**
     * 故障设备列表(可选)
     */
    private List<String> info;

    public SipKeepaliveDesc(String requestDeviceID, String requestDeviceAddress,String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}
