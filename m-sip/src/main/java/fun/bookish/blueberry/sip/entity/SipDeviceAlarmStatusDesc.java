package fun.bookish.blueberry.sip.entity;

/**
 * 设备报警状态描述
 */
public class SipDeviceAlarmStatusDesc {

    /**
     * 报警设备编码(必选)
     */
    private final String deviceId;
    /**
     * 报警设备状态(必选)
     *      ONDUTY
     *      OFFDUTY
     *      ALARM
     */
    private final String dutyStatus;

    public SipDeviceAlarmStatusDesc(String deviceId, String dutyStatus) {
        this.deviceId = deviceId;
        this.dutyStatus = dutyStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDutyStatus() {
        return dutyStatus;
    }
}
