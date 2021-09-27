package fun.bookish.blueberry.sip.entity;


import fun.bookish.blueberry.sip.event.SipEventTypeEnum;

import java.util.List;

/**
 * 设备信息描述
 */
public class SipDeviceInfoDesc extends SipDeviceDesc{

    /**
     * 设备名称(必选)
     */
    private String deviceName;
    /**
     * 查询结果(必选):
     *      OK
     *      ERROR
     */
    private String result;
    /**
     * 设备生产商(可选)
     */
    private String manufacturer;
    /**
     * 设备型号(可选)
     */
    private String model;
    /**
     * 设备固件版本(可选)
     */
    private String firmware;
    /**
     * 视频输入通道数(可选)
     */
    private Integer channel;
    /**
     * 扩展信息(可选)
     */
    private List<String> info;

    public SipDeviceInfoDesc(String requestDeviceID, String requestDeviceAddress, String deviceId, String registerServer, SipEventTypeEnum eventType) {
        super(requestDeviceID, requestDeviceAddress, deviceId, registerServer, eventType);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}
