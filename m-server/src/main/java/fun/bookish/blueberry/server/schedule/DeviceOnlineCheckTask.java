package fun.bookish.blueberry.server.schedule;

import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.core.utils.NetUtils;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.entity.DeviceType;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.hook.HttpHookExecutor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备在线检测任务
 */
@Component
public class DeviceOnlineCheckTask implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoNoReaderCheckTask.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private HttpHookExecutor httpHookExecutor;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("设备在线状态检测任务开始执行...");
        long start = System.currentTimeMillis();
        try {
            List<String> on2off = new ArrayList<>();
            List<String> off2on = new ArrayList<>();
            // 查询所有设备
            List<DevicePO> devices = deviceService.query().list();
            // 根据设备过期时长与最后心跳时间，判断设备在线状态
            for (DevicePO device : devices) {
                String deviceType = device.getType();
                if (DeviceType.GB.equals(deviceType)) {
                    handleGbDevice(device, on2off, off2on);
                } else if (DeviceType.RTSP.equals(deviceType)) {
                    handleRtspDevice(device, on2off, off2on);
                }
            }
            LOGGER.info("设备在线状态检测任务执行完成({}ms). on->off:{}, off->on:{}", System.currentTimeMillis() - start, JsonUtils.encode(on2off), JsonUtils.encode(off2on));
        } catch (Exception e) {
            LOGGER.error("设备在线状态检测任务异常", e);
        }
    }

    /**
     * 处理rtsp设备
     *
     * @param device
     * @param on2off
     * @param off2on
     */
    private void handleRtspDevice(DevicePO device, List<String> on2off, List<String> off2on) {
        String deviceId = device.getId();
        try {
            boolean online = NetUtils.ping(device.getRemoteIp(), 10);
            updateDeviceStatus(device, online, on2off, off2on);
        } catch (Exception e) {
            LOGGER.error("设备[{}]在线状态检测异常, deviceType:{}", deviceId, device.getType(), e);
        }
    }

    /**
     * 处理国标设备
     *
     * @param device
     * @param on2off
     * @param off2on
     */
    private void handleGbDevice(DevicePO device, List<String> on2off, List<String> off2on) {
        String deviceId = device.getId();
        try {
            Integer expires = device.getExpires();
            String lastKeepaliveAt = device.getLastKeepaliveAt();
            boolean online = DateUtils.parseDateStr(lastKeepaliveAt, "yyyy-MM-dd HH:mm:ss").plus(expires, ChronoUnit.SECONDS).isAfter(LocalDateTime.now());
            updateDeviceStatus(device, online, on2off, off2on);
        } catch (Exception e) {
            LOGGER.error("设备[{}]在线状态检测异常, deviceType:{}", deviceId, device.getType(), e);
        }
    }

    /**
     * 更新设备状态
     * @param device
     * @param online
     * @param on2off
     * @param off2on
     */
    private void updateDeviceStatus(DevicePO device, boolean online, List<String> on2off, List<String> off2on) {
        String deviceId = device.getId();
        Integer onlineStatus = device.getOnline();
        if (online) {
            // 更新数据库中的设备状态
            if (onlineStatus != 1) {
                DevicePO updateDevice = new DevicePO();
                updateDevice.setId(deviceId);
                updateDevice.setOnline(1);
                deviceService.updateById(updateDevice);
                off2on.add(deviceId);

            }
            // 触发设备在线回调
            HttpHookExecutor.DeviceOnline hookData = new HttpHookExecutor.DeviceOnline();
            hookData.setDeviceId(deviceId);
            httpHookExecutor.onDeviceOnline(hookData);
            // 处理设备下的通道
            List<ChannelPO> channels = channelService.query().eq("device_id", deviceId).list();
            for (ChannelPO channel: channels) {
                String channelId = channel.getId();
                // 更新数据库中的通道状态
                if (!"ON".equals(channel.getStatus())) {
                    ChannelPO updateChannel = new ChannelPO();
                    updateChannel.setId(channelId);
                    updateChannel.setDeviceId(deviceId);
                    updateChannel.setStatus("ON");
                    channelService.addOrUpdate(updateChannel);
                }
                // 触发通道在线回调
                if (0 == channel.getParental()) {
                    HttpHookExecutor.ChannelOnline channelOnline = new HttpHookExecutor.ChannelOnline();
                    channelOnline.setDeviceId(deviceId);
                    channelOnline.setChannelId(channelId);
                    httpHookExecutor.onChannelOnline(channelOnline);
                }
            }
        } else {
            // 更新数据库中的设备状态
            if (onlineStatus != 0) {
                DevicePO updateDevice = new DevicePO();
                updateDevice.setId(deviceId);
                updateDevice.setOnline(0);
                deviceService.updateById(updateDevice);
                on2off.add(deviceId);
            }
            // 触发设备离线回调
            HttpHookExecutor.DeviceOffline hookData = new HttpHookExecutor.DeviceOffline();
            hookData.setDeviceId(deviceId);
            httpHookExecutor.onDeviceOffline(hookData);
            // 处理设备下的通道
            List<ChannelPO> channels = channelService.query().eq("device_id", deviceId).list();
            for (ChannelPO channel: channels) {
                String channelId = channel.getId();
                // 更新数据库中的通道状态
                if (!"OFF".equals(channel.getStatus())) {
                    ChannelPO updateChannel = new ChannelPO();
                    updateChannel.setId(channelId);
                    updateChannel.setDeviceId(deviceId);
                    updateChannel.setStatus("OFF");
                    channelService.addOrUpdate(updateChannel);
                }
                // 触发通道离线回调
                if (0 == channel.getParental()) {
                    HttpHookExecutor.ChannelOffline channelOffline = new HttpHookExecutor.ChannelOffline();
                    channelOffline.setDeviceId(deviceId);
                    channelOffline.setChannelId(channelId);
                    httpHookExecutor.onChannelOffline(channelOffline);
                }
            }
        }
    }

}
