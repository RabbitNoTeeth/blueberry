package fun.bookish.blueberry.sip.entity;


import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

import java.util.List;

/**
 * 设备信息描述
 */
public class SipDeviceStatusDesc extends SipDeviceDesc{

    /**
     * 查询结果标志(必选):
     *      OK
     *      ERROR
     */
    private String result;
    /**
     * 是否在线(必选)：
     *      ONLINE
     *      OFFLINE
     */
    private String online;
    /**
     * 是否正常工作(必选)：
     *      OK
     *      ERROR
     */
    private String status;
    /**
     * 不正常工作原因(可选)
     */
    private String reason;
    /**
     * 是否编码(可选)
     *      ON
     *      OFF
     */
    private String encode;
    /**
     * 是否录像(可选)
     *      ON
     *      OFF
     */
    private String record;
    /**
     * 设备时间和日期(可选)
     */
    private String deviceTime;
    /**
     * 报警设备状态列表(可选)
     */
    private List<SipDeviceAlarmStatusDesc> alarmStatus;
    /**
     * 扩展信息(可选)
     */
    private List<String> info;

    public SipDeviceStatusDesc(String requestDeviceID, String requestDeviceAddress, String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    public List<SipDeviceAlarmStatusDesc> getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(List<SipDeviceAlarmStatusDesc> alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}
