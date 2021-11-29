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
     * 触发设备通道删除钩子
     * @param deviceId
     * @param channelId
     * @return
     */
    @PostMapping("/stream/qualityDetect")
    public Object hookOnStreamQualityDetect(@NotBlank(message = "设备ID不能为空") String deviceId, @NotBlank(message = "通道ID不能为空") String channelId) {
        return devService.streamQualityDetect(deviceId, channelId);
    }

}
