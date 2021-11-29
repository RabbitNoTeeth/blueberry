package fun.bookish.blueberry.server.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.NetUtils;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.entity.DeviceType;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.sip.MySipEventListener;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private MySipEventListener sipEventListener;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("设备在线状态检测任务开始执行...");
        long start = System.currentTimeMillis();
        try {
            // 查询所有设备
            List<DevicePO> devices = deviceService.query().list();
            // 根据设备过期时长与最后心跳时间，判断设备在线状态
            for (DevicePO device : devices) {
                String deviceType = device.getType();
                if (DeviceType.GB.equals(deviceType)) {
                    handleGbDevice(device);
                } else if (DeviceType.RTSP.equals(deviceType)) {
                    handleRtspDevice(device);
                }
            }
            LOGGER.info("设备在线状态检测任务执行完成({}ms).", System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("设备在线状态检测任务异常", e);
        }
    }

    /**
     * 处理rtsp设备
     *
     * @param device
     */
    private void handleRtspDevice(DevicePO device) {
        String deviceId = device.getId();
        try {
            boolean online = NetUtils.ping(device.getRemoteIp(), 10);
            // 更新数据库
            DevicePO updateDevice = new DevicePO();
            updateDevice.setId(deviceId);
            updateDevice.setOnline(online ? 1 : 0);
            updateDevice.setUpdatedAt(DateUtils.getNowDateTimeStr());
            deviceService.updateById(updateDevice);
            ChannelPO updateChannel = new ChannelPO();
            updateChannel.setOnline(online? "ONLINE" : "OFFLINE");
            updateChannel.setUpdatedAt(DateUtils.getNowDateTimeStr());
            channelService.update(updateChannel, new QueryWrapper<ChannelPO>().eq("device_id", deviceId));
        } catch (Exception e) {
            LOGGER.error("设备[{}]在线状态检测异常, deviceType:{}", deviceId, device.getType(), e);
        }
    }

    /**
     * 处理国标设备
     *
     * @param device
     */
    private void handleGbDevice(DevicePO device) {
        String deviceId = device.getId();
        try {
            // 先检查未正常注销的设备
            Integer expires = device.getExpires();
            String lastKeepaliveAt = device.getLastKeepaliveAt();
            boolean online = DateUtils.parseDateStr(lastKeepaliveAt, "yyyy-MM-dd HH:mm:ss").plus(expires, ChronoUnit.SECONDS).isAfter(LocalDateTime.now());
            if (online) {
                sipEventListener.queryDeviceStatus();
            } else {
                // 更新数据库
                DevicePO updateDevice = new DevicePO();
                updateDevice.setId(deviceId);
                updateDevice.setOnline(0);
                updateDevice.setUpdatedAt(DateUtils.getNowDateTimeStr());
                deviceService.updateById(updateDevice);
                ChannelPO updateChannel = new ChannelPO();
                updateChannel.setOnline("OFFLINE");
                updateChannel.setUpdatedAt(DateUtils.getNowDateTimeStr());
                channelService.update(updateChannel, new QueryWrapper<ChannelPO>().eq("device_id", deviceId));
            }
        } catch (Exception e) {
            LOGGER.error("设备[{}]在线状态检测异常, deviceType:{}", deviceId, device.getType(), e);
        }
    }

}
