package fun.bookish.blueberry.server.dev;

import fun.bookish.blueberry.server.dev.service.DevService;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/api/v1/dev")
public class DevController {

    @Autowired
    private DevService devService;

    /**
     * 发送sip命令，查新设备列表
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/sip/catalog")
    public Object sipCatalog(SipQueryCatalogParam param) throws Exception {
        return devService.sipCatalog(param);
    }

    /**
     * 触发设备上线钩子
     * @param deviceId
     * @return
     */
    @PostMapping("/hook/deviceOnline")
    public Object hookOnDeviceOnline(@NotBlank(message = "设备ID不能为空") String deviceId) {
        return devService.hookOnDeviceOnline(deviceId);
    }

    /**
     * 触发设备离线钩子
     * @param deviceId
     * @return
     */
    @PostMapping("/hook/deviceOffline")
    public Object hookOnDeviceOffline(@NotBlank(message = "设备ID不能为空") String deviceId) {
        return devService.hookOnDeviceOffline(deviceId);
    }

    /**
     * 触发设备更新钩子
     * @param deviceId
     * @return
     */
    @PostMapping("/hook/deviceUpdate")
    public Object hookOnDeviceUpdate(@NotBlank(message = "设备ID不能为空") String deviceId) {
        return devService.hookOnDeviceUpdate(deviceId);
    }

    /**
     * 触发设备删除钩子
     * @param deviceId
     * @return
     */
    @PostMapping("/hook/deviceRemove")
    public Object hookOnDeviceRemove(@NotBlank(message = "设备ID不能为空") String deviceId) {
        return devService.hookOnDeviceRemove(deviceId);
    }

    /**
     * 触发设备通道上线钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/hook/channelOnline")
    public Object hookOnChannelOnline(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.hookOnChannelOnline(deviceId, channelId);
    }

    /**
     * 触发设备通道离线钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/hook/channelOffline")
    public Object hookOnChannelOffline(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.hookOnChannelOffline(deviceId, channelId);
    }

    /**
     * 触发设备通道更新钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/hook/channelUpdate")
    public Object hookOnChannelUpdate(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.hookOnChannelUpdate(deviceId, channelId);
    }

    /**
     * 触发设备通道删除钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/hook/channelRemove")
    public Object hookOnChannelRemove(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.hookOnChannelRemove(deviceId, channelId);
    }

    /**
     * 触发设备通道删除钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/hook/streamQualityDetect")
    public Object hookOnStreamQualityDetect(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.hookOnStreamQualityDetect(deviceId, channelId);
    }

}
