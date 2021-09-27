package fun.bookish.blueberry.sip.command.executor.query.param;

import fun.bookish.blueberry.sip.command.executor.param.SipCommandParam;

/**
 * 设备目录查询参数
 */
public class SipQueryCatalogParam extends SipCommandParam {

    private final String queryDeviceId;

    private String startTime;

    private String endTime;

    public SipQueryCatalogParam(String deviceId, String deviceAddress, String queryDeviceId, String transport) {
        super(deviceId, deviceAddress, transport);
        this.queryDeviceId = queryDeviceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getQueryDeviceId() {
        return queryDeviceId;
    }
}
