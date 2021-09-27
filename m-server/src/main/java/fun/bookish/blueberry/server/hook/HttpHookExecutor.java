package fun.bookish.blueberry.server.hook;

import fun.bookish.blueberry.core.utils.BeanUtils;
import fun.bookish.blueberry.core.utils.HttpClientUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.hook.conf.HttpHookProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HttpHookExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHookExecutor.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private HttpHookProperties httpHookProperties;

    /**
     * 视频质量检测
     *
     * @param data
     */
    public void onVideoQualityDetect(VideoQualityDetect data) {
        doRequest(httpHookProperties.getOnVideoQualityDetect(), data, "onVideoQualityDetect");
    }

    /**
     * 设备上线
     *
     * @param data
     */
    public void onDeviceOnline(DeviceOnline data) {
        doRequest(httpHookProperties.getOnDeviceOnline(), data, "onDeviceOnline");
    }

    /**
     * 设备离线
     *
     * @param data
     */
    public void onDeviceOffline(DeviceOffline data) {
        doRequest(httpHookProperties.getOnDeviceOffline(), data, "onDeviceOffline");
    }

    /**
     * 设备新增
     *
     * @param data
     */
    public void onDeviceCreated(DeviceEntity data) {
        doRequest(httpHookProperties.getOnDeviceCreated(), data, "onDeviceCreated");
    }

    /**
     * 设备更新
     *
     * @param data
     */
    public void onDeviceUpdated(DeviceEntity data) {
        doRequest(httpHookProperties.getOnDeviceUpdated(), data, "onDeviceUpdated");
    }

    /**
     * 设备删除
     *
     * @param data
     */
    public void onDeviceRemoved(DeviceRemove data) {
        doRequest(httpHookProperties.getOnDeviceRemoved(), data, "onDeviceRemoved");
    }

    /**
     * 设备通道上线
     *
     * @param data
     */
    public void onChannelOnline(ChannelOnline data) {
        doRequest(httpHookProperties.getOnChannelOnline(), data, "onChannelOnline");
    }

    /**
     * 设备通道离线
     *
     * @param data
     */
    public void onChannelOffline(ChannelOffline data) {
        doRequest(httpHookProperties.getOnChannelOffline(), data, "onChannelOffline");
    }

    /**
     * 设备通道新增
     *
     * @param data
     */
    public void onChannelCreated(ChannelEntity data) {
        doRequest(httpHookProperties.getOnChannelCreated(), data, "onChannelCreated");
    }

    /**
     * 设备通道更新
     *
     * @param data
     */
    public void onChannelUpdated(ChannelEntity data) {
        doRequest(httpHookProperties.getOnChannelUpdated(), data, "onChannelUpdated");
    }

    /**
     * 设备通道删除
     *
     * @param data
     */
    public void onChannelRemoved(ChannelRemove data) {
        doRequest(httpHookProperties.getOnChannelRemoved(), data, "onChannelRemoved");
    }

    /**
     * 执行调用
     *
     * @param url
     * @param tag
     */
    private void doRequest(String url, Object data, String tag) {
        if (!httpHookProperties.getEnable()) {
            return;
        }
        executorService.execute(() -> {
            String dataJsonStr = JsonUtils.encode(data);
            try {
                if (StringUtils.isBlank(url)) {
                    return;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("data", dataJsonStr);
                String response = HttpClientUtils.requestForString(url, "POST", null, params);
                LOGGER.info("HttpHook.{} 执行结束, targetUrl:{}, data:{}, response:{}", tag, url, dataJsonStr, response);
            } catch (Exception e) {
                LOGGER.error("HttpHook.{} 执行异常, targetUrl:{}, data:{}", tag, url, dataJsonStr, e);
            }
        });
    }

    public static class DeviceEntity {
        private String id;
        private String name;
        private String manufacturer;
        private String model;
        private String firmware;
        private String type;
        private String commandTransport;
        private String remoteIp;
        private Integer remotePort;
        private Integer online;
        private String sipServerAddress;
        private Integer expires;
        private String lastRegisterAt;
        private String lastKeepaliveAt;
        private String createdAt;
        private String updatedAt;
        private String serverIp;
        private Integer serverPort;

        public static DeviceEntity mapFromDevicePO(DevicePO devicePO) {
            return BeanUtils.convert(devicePO, DeviceEntity.class);
        }

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

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public Integer getServerPort() {
            return serverPort;
        }

        public void setServerPort(Integer serverPort) {
            this.serverPort = serverPort;
        }
    }

    public static class DeviceOnline {
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    public static class DeviceOffline {
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    public static class DeviceRemove {
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    public static class ChannelOnline {
        private String deviceId;
        private String channelId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
    }

    public static class ChannelOffline {
        private String deviceId;
        private String channelId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
    }

    public static class ChannelEntity {
        private String id;
        private String deviceId;
        private String name;
        private String manufacturer;
        private String model;
        private String owner;
        private String civilCode;
        private String block;
        private String address;
        private Integer parental;
        private String parentId;
        private String parentChannelId;
        private Integer safetyWay;
        private Integer registerWay;
        private String certNum;
        private Integer certifiable;
        private Integer errCode;
        private String endTime;
        private Integer secrecy;
        private String ipAddress;
        private Integer port;
        private String password;
        private String status;
        private Double longitude;
        private Double latitude;
        private Integer ptzType;
        private Integer positionType;
        private Integer roomType;
        private Integer useType;
        private Integer supplyLightType;
        private Integer directionType;
        private String resolution;
        private String businessGroupId;
        private String downloadSpeed;
        private Integer svcSpaceSupportMode;
        private Integer svcTimeSupportMode;
        private String rtsp;
        private Integer online;
        private String createdAt;
        private String updatedAt;
        private DeviceEntity device;

        public static ChannelEntity mapFromChannelPO(ChannelPO channelPO) {
            return BeanUtils.convert(channelPO, ChannelEntity.class);
        }

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

        public Integer getOnline() {
            return online;
        }

        public void setOnline(Integer online) {
            this.online = online;
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

        public DeviceEntity getDevice() {
            return device;
        }

        public void setDevice(DeviceEntity device) {
            this.device = device;
        }
    }

    public static class ChannelRemove {
        private String deviceId;
        private String channelId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
    }

    public static class VideoQualityDetect {
        private String deviceId;
        private String channelId;
        private boolean hasError;
        private String error;
        private String snapshot;
        private String detail;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public boolean isHasError() {
            return hasError;
        }

        public void setHasError(boolean hasError) {
            this.hasError = hasError;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getSnapshot() {
            return snapshot;
        }

        public void setSnapshot(String snapshot) {
            this.snapshot = snapshot;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

}
