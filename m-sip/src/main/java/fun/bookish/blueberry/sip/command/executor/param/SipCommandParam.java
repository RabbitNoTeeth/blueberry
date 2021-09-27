package fun.bookish.blueberry.sip.command.executor.param;

/**
 * 设备目录查询参数
 */
public class SipCommandParam {

    /**
     * 目标设备ID
     *      如果当前服务作为上级与其他sip服务平台进行对接时，请求发送目标可能是平台，此时就需要使用平台ID；
     *      否则使用要查询的设备ID，直接发送请求到被查询设备
     */
    protected final String deviceId;
    /**
     * 目标设备地址
     *      如果当前服务作为上级与其他sip服务平台进行对接时，请求发送目标可能是平台，此时就需要使用平台地址；
     *      否则使用要查询的设备地址，直接发送请求到被查询设备
     */
    protected final String deviceAddress;
    /**
     * 通信协议
     */
    protected final String transport;

    public SipCommandParam(String deviceId, String deviceAddress, String transport) {
        this.deviceId = deviceId;
        this.deviceAddress = deviceAddress;
        this.transport = transport;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public String getTransport() {
        return transport;
    }

}
