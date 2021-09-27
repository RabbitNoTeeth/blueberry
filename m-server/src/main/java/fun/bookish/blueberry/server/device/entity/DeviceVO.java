package fun.bookish.blueberry.server.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_device")
public class DeviceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备编码", required = true)
    private String id;

    @ApiModelProperty(value = "设备名称", required = true)
    private String name;

    @ApiModelProperty(value = "设备生产商")
    private String manufacturer;

    @ApiModelProperty(value = "设备型号")
    private String model;

    @ApiModelProperty(value = "设备固件版本")
    private String firmware;

    @ApiModelProperty(value = "设备类型", required = true, example = "GB, RTSP")
    private String type;

    @ApiModelProperty(value = "SIP命令通信协议（仅限于GB设备）")
    private String commandTransport;

    @ApiModelProperty(value = "设备ip地址", required = true)
    private String remoteIp;

    @ApiModelProperty(value = "设备端口号", required = true, example = "5060")
    private Integer remotePort;

    @ApiModelProperty(value = "是否在线（0：否，1：是）", required = true, example = "0")
    private Integer online;

    @ApiModelProperty(value = "sip服务器地址（仅限于GB设备）")
    private String sipServerAddress;

    @ApiModelProperty(value = "设备过期时长（单位：秒，仅限于GB设备）", example = "3600")
    private Integer expires;

    @ApiModelProperty(value = "最后一次注册时间（仅限于GB设备）")
    private String lastRegisterAt;

    @ApiModelProperty(value = "最后一次心跳时间（仅限于GB设备）")
    private String lastKeepaliveAt;

    @ApiModelProperty(value = "创建时间")
    private String createdAt;

    @ApiModelProperty(value = "更新时间")
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommandTransport() {
        return commandTransport;
    }

    public void setCommandTransport(String commandTransport) {
        this.commandTransport = commandTransport;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getSipServerAddress() {
        return sipServerAddress;
    }

    public void setSipServerAddress(String sipServerAddress) {
        this.sipServerAddress = sipServerAddress;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public String getLastRegisterAt() {
        return lastRegisterAt;
    }

    public void setLastRegisterAt(String lastRegisterAt) {
        this.lastRegisterAt = lastRegisterAt;
    }

    public String getLastKeepaliveAt() {
        return lastKeepaliveAt;
    }

    public void setLastKeepaliveAt(String lastKeepaliveAt) {
        this.lastKeepaliveAt = lastKeepaliveAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DeviceVO{" +
        "id=" + id +
        ", name=" + name +
        ", manufacturer=" + manufacturer +
        ", model=" + model +
        ", firmware=" + firmware +
        ", type=" + type +
        ", commandTransport=" + commandTransport +
        ", remoteIp=" + remoteIp +
        ", remotePort=" + remotePort +
        ", online=" + online +
        ", sipServerAddress=" + sipServerAddress +
        ", expires=" + expires +
        ", lastRegisterAt=" + lastRegisterAt +
        ", lastKeepaliveAt=" + lastKeepaliveAt +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
