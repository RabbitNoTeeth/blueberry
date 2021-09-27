package fun.bookish.blueberry.sip.command.executor.query.param;

import fun.bookish.blueberry.sip.command.executor.param.SipCommandParam;

/**
 * 设备状态查询参数
 */
public class SipQueryDeviceStatusParam extends SipCommandParam {

    private final String queryDeviceId;

    public SipQueryDeviceStatusParam(String deviceId, String deviceAddress, String queryDeviceId, String protocolType) {
        super(deviceId, deviceAddress, protocolType);
        this.queryDeviceId = queryDeviceId;
    }

    public String getQueryDeviceId() {
        return queryDeviceId;
    }
}
