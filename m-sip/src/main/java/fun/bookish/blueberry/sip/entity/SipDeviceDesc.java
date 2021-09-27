package fun.bookish.blueberry.sip.entity;

import fun.bookish.blueberry.sip.event.SipEventTypeEnum;
import fun.bookish.blueberry.sip.utils.DateUtils;

/**
 * 设备描述基类
 */
public class SipDeviceDesc {

    /**
     * 请求来源设备id
     */
    protected final String fromDeviceId;
    /**
     * 请求来源设备地址
     */
    protected final String fromDeviceAddress;
    /**
     * 设备id(必选)
     */
    protected final String deviceID;
    /**
     * 更新日期
     */
    protected final String updateTime;
    /**
     * 注册服务器
     */
    protected final String sipServerHost;
    /**
     * sip事件
     */
    protected final SipEventTypeEnum eventType;

    public SipDeviceDesc(String fromDeviceId, String fromDeviceAddress, String deviceID, String sipServerHost, SipEventTypeEnum eventType) {
        this.fromDeviceId = fromDeviceId;
        this.fromDeviceAddress = fromDeviceAddress;
        this.deviceID = deviceID;
        this.eventType = eventType;
        this.updateTime = DateUtils.getNowDateTimeStr();
        this.sipServerHost = sipServerHost;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getSipServerHost() {
        return sipServerHost;
    }

    public SipEventTypeEnum getEventType() {
        return eventType;
    }

    public String getFromDeviceId() {
        return fromDeviceId;
    }

    public String getFromDeviceAddress() {
        return fromDeviceAddress;
    }
}
