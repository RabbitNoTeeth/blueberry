package fun.bookish.blueberry.server.dev.service;

import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.hook.HttpHookExecutor;
import fun.bookish.blueberry.server.videoqualitydetect.detect.entity.VideoStreamQualityDetectResult;
import fun.bookish.blueberry.server.videoqualitydetect.detect.VideoStreamDetectService;
import fun.bookish.blueberry.server.sip.conf.SipProperties;
import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DevService {

    @Autowired
    private SipServer sipServer;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private HttpHookExecutor httpHookExecutor;
    @Autowired
    private VideoStreamDetectService videoStreamDetectService;
    @Autowired
    private SipProperties sipProperties;
    @Value("${server.port}")
    private Integer serverPort;

    /**
     * 发送sip命令，查询设备列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    public Object sipCatalog(SipQueryCatalogParam param) throws Exception {
        SipQueryCommandExecutor sipQueryCommandExecutor = sipServer.getSipCommandExecutorManager().getSipQueryCommandExecutor();
        sipQueryCommandExecutor.queryCatalog(param);
        return "success";
    }

    /**
     * 触发设备上线钩子
     *
     * @param deviceId
     * @return
     */
    public Object hookOnDeviceOnline(String deviceId) {
        HttpHookExecutor.DeviceOnline data = new HttpHookExecutor.DeviceOnline();
        data.setDeviceId(deviceId);
        return data;
    }

    /**
     * 触发设备离线钩子
     *
     * @param deviceId
     * @return
     */
    public Object hookOnDeviceOffline(String deviceId) {
        HttpHookExecutor.DeviceOffline data = new HttpHookExecutor.DeviceOffline();
        data.setDeviceId(deviceId);
        httpHookExecutor.onDeviceOffline(data);
        return data;
    }

    /**
     * 触发设备更新钩子
     *
     * @param deviceId
     * @return
     */
    public Object hookOnDeviceUpdate(String deviceId) {
        HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(deviceService.queryById(deviceId));
        device.setServerIp(sipProperties.getHost());
        device.setServerPort(serverPort);
        httpHookExecutor.onDeviceUpdated(device);
        return device;
    }

    /**
     * 触发设备删除钩子
     *
     * @param deviceId
     * @return
     */
    public Object hookOnDeviceRemove(String deviceId) {
        HttpHookExecutor.DeviceRemove data = new HttpHookExecutor.DeviceRemove();
        data.setDeviceId(deviceId);
        httpHookExecutor.onDeviceRemoved(data);
        return data;
    }
    /**
     * 触发设备通道上线钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object hookOnChannelOnline(String deviceId, String channelId) {
        HttpHookExecutor.ChannelOnline data = new HttpHookExecutor.ChannelOnline();
        data.setDeviceId(deviceId);
        data.setChannelId(channelId);
        httpHookExecutor.onChannelOnline(data);
        return data;
    }
    /**
     * 触发设备通道离线钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object hookOnChannelOffline(String deviceId, String channelId) {
        HttpHookExecutor.ChannelOffline data = new HttpHookExecutor.ChannelOffline();
        data.setDeviceId(deviceId);
        data.setChannelId(channelId);
        httpHookExecutor.onChannelOffline(data);
        return data;
    }

    /**
     * 触发设备通道更新钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object hookOnChannelUpdate(String deviceId, String channelId) {
        HttpHookExecutor.ChannelEntity channel = HttpHookExecutor.ChannelEntity.mapFromChannelPO(channelService.queryByIdAndDeviceId(channelId, deviceId));
        HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(deviceService.queryById(deviceId));
        device.setServerIp(sipProperties.getHost());
        device.setServerPort(serverPort);
        channel.setDevice(device);
        httpHookExecutor.onChannelUpdated(channel);
        return channel;
    }

    /**
     * 触发设备通道删除钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object hookOnChannelRemove(String deviceId, String channelId) {
        HttpHookExecutor.ChannelRemove data = new HttpHookExecutor.ChannelRemove();
        data.setDeviceId(deviceId);
        data.setChannelId(channelId);
        httpHookExecutor.onChannelRemoved(data);
        return data;
    }

    /**
     * 触发设备图像质量检测钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object hookOnStreamQualityDetect(String deviceId, String channelId) {
        VideoStreamQualityDetectResult data = videoStreamDetectService.detectStreamQuality(deviceId, channelId, DateUtils.getNowDateStr("yyyyMMddHHmmss"));
        return data;
    }


}
