package fun.bookish.blueberry.server.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_device")
public class DevicePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编码
     */
    private String id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备生产商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备固件版本
     */
    private String firmware;

    /**
     * 设备类型
     */
    private String type;

    /**
     * SIP命令通信协议（仅限于GB设备）
     */
    private String commandTransport;

    /**
     * 设备ip地址
     */
    private String remoteIp;

    /**
     * 设备端口号
     */
    private Integer remotePort;

    /**
     * 是否在线
     */
    private Integer online;

    /**
     * sip服务器地址（仅限于GB设备）
     */
    private String sipServerAddress;

    /**
     * 设备过期时长
     */
    private Integer expires;

    /**
     * 最后一次注册时间（仅限于GB设备）
     */
    private String lastRegisterAt;

    /**
     * 最后一次心跳时间（仅限于GB设备）
     */
    private String lastKeepaliveAt;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 更新时间
     */
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
        return "DevicePO{" +
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
