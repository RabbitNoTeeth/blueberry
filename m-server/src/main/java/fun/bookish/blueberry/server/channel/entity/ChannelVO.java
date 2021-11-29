package fun.bookish.blueberry.server.channel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备通道表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_channel")
public class ChannelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "通道编码", required = true)
    private String id;

    @ApiModelProperty(value = "设备/区域/系统编码", required = true)
    private String deviceId;

    @ApiModelProperty(value = "设备/区域/系统名称", required = true)
    private String name;

    @ApiModelProperty(value = "设备厂商")
    private String manufacturer;

    @ApiModelProperty(value = "设备型号")
    private String model;

    @ApiModelProperty(value = "设备归属")
    private String owner;

    @ApiModelProperty(value = "设备固件版本")
    private String firmware;

    @ApiModelProperty(value = "行政区域")
    private String civilCode;

    @ApiModelProperty(value = "警区")
    private String block;

    @ApiModelProperty(value = "安装地址")
    private String address;

    @ApiModelProperty(value = "是否有子设备", required = true, example = "0")
    private Integer parental;

    @ApiModelProperty(value = "父设备ID")
    private String parentId;

    @ApiModelProperty(value = "父通道ID")
    private String parentChannelId;

    @ApiModelProperty(value = "信令安全模式", example = "0")
    private Integer safetyWay;

    @ApiModelProperty(value = "注册方式", example = "2")
    private Integer registerWay;

    @ApiModelProperty(value = "证书序列号")
    private String certNum;

    @ApiModelProperty(value = "证书有效标识", example = "1")
    private Integer certifiable;

    @ApiModelProperty(value = "无效原因码", example = "0")
    private Integer errCode;

    @ApiModelProperty(value = "证书终止有效期")
    private String endTime;

    @ApiModelProperty(value = "保密属性", example = "0")
    private Integer secrecy;

    @ApiModelProperty(value = "通道IP")
    private String ipAddress;

    @ApiModelProperty(value = "通道端口", example = "5060")
    private Integer port;

    @ApiModelProperty(value = "设备口令")
    private String password;

    @ApiModelProperty(value = "设备状态", required = true)
    private String status;

    @ApiModelProperty(value = "在线状态", required = true)
    private String online;

    @ApiModelProperty(value = "是否编码", required = true)
    private String encode;

    @ApiModelProperty(value = "是否录像", required = true)
    private String record;

    @ApiModelProperty(value = "经度", example = "0.0")
    private Double longitude;

    @ApiModelProperty(value = "纬度", example = "0.0")
    private Double latitude;

    @ApiModelProperty(value = "摄像机类型扩展", example = "1")
    private Integer ptzType;

    @ApiModelProperty(value = "摄像机位置类型扩展", example = "1")
    private Integer positionType;

    @ApiModelProperty(value = "安装位置", example = "1")
    private Integer roomType;

    @ApiModelProperty(value = "摄像机用途", example = "1")
    private Integer useType;

    @ApiModelProperty(value = "摄像机补光", example = "1")
    private Integer supplyLightType;

    @ApiModelProperty(value = "摄像机监视方位", example = "1")
    private Integer directionType;

    @ApiModelProperty(value = "摄像机支持的分辨率")
    private String resolution;

    @ApiModelProperty(value = "虚拟组织所属的业务分组ID")
    private String businessGroupId;

    @ApiModelProperty(value = "下载倍速范围")
    private String downloadSpeed;

    @ApiModelProperty(value = "空域编码能力", example = "0")
    private Integer svcSpaceSupportMode;

    @ApiModelProperty(value = "时域编码能力", example = "0")
    private Integer svcTimeSupportMode;

    @ApiModelProperty(value = "rtsp取流地址")
    private String rtsp;

    @ApiModelProperty(value = "创建时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String createdAt;

    @ApiModelProperty(value = "更新时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCivilCode() {
        return civilCode;
    }

    public void setCivilCode(String civilCode) {
        this.civilCode = civilCode;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getParental() {
        return parental;
    }

    public void setParental(Integer parental) {
        this.parental = parental;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentChannelId() {
        return parentChannelId;
    }

    public void setParentChannelId(String parentChannelId) {
        this.parentChannelId = parentChannelId;
    }

    public Integer getSafetyWay() {
        return safetyWay;
    }

    public void setSafetyWay(Integer safetyWay) {
        this.safetyWay = safetyWay;
    }

    public Integer getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(Integer registerWay) {
        this.registerWay = registerWay;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public Integer getCertifiable() {
        return certifiable;
    }

    public void setCertifiable(Integer certifiable) {
        this.certifiable = certifiable;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(Integer secrecy) {
        this.secrecy = secrecy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPtzType() {
        return ptzType;
    }

    public void setPtzType(Integer ptzType) {
        this.ptzType = ptzType;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getSupplyLightType() {
        return supplyLightType;
    }

    public void setSupplyLightType(Integer supplyLightType) {
        this.supplyLightType = supplyLightType;
    }

    public Integer getDirectionType() {
        return directionType;
    }

    public void setDirectionType(Integer directionType) {
        this.directionType = directionType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getBusinessGroupId() {
        return businessGroupId;
    }

    public void setBusinessGroupId(String businessGroupId) {
        this.businessGroupId = businessGroupId;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Integer getSvcSpaceSupportMode() {
        return svcSpaceSupportMode;
    }

    public void setSvcSpaceSupportMode(Integer svcSpaceSupportMode) {
        this.svcSpaceSupportMode = svcSpaceSupportMode;
    }

    public Integer getSvcTimeSupportMode() {
        return svcTimeSupportMode;
    }

    public void setSvcTimeSupportMode(Integer svcTimeSupportMode) {
        this.svcTimeSupportMode = svcTimeSupportMode;
    }

    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
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

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    @Override
    public String toString() {
        return "ChannelVO{" +
        "id=" + id +
        ", deviceId=" + deviceId +
        ", name=" + name +
        ", manufacturer=" + manufacturer +
        ", model=" + model +
        ", owner=" + owner +
        ", civilCode=" + civilCode +
        ", block=" + block +
        ", address=" + address +
        ", parental=" + parental +
        ", parentId=" + parentId +
        ", parentChannelId=" + parentChannelId +
        ", safetyWay=" + safetyWay +
        ", registerWay=" + registerWay +
        ", certNum=" + certNum +
        ", certifiable=" + certifiable +
        ", errCode=" + errCode +
        ", endTime=" + endTime +
        ", secrecy=" + secrecy +
        ", ipAddress=" + ipAddress +
        ", port=" + port +
        ", password=" + password +
        ", status=" + status +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", ptzType=" + ptzType +
        ", positionType=" + positionType +
        ", roomType=" + roomType +
        ", useType=" + useType +
        ", supplyLightType=" + supplyLightType +
        ", directionType=" + directionType +
        ", resolution=" + resolution +
        ", businessGroupId=" + businessGroupId +
        ", downloadSpeed=" + downloadSpeed +
        ", svcSpaceSupportMode=" + svcSpaceSupportMode +
        ", svcTimeSupportMode=" + svcTimeSupportMode +
        ", rtsp=" + rtsp +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
