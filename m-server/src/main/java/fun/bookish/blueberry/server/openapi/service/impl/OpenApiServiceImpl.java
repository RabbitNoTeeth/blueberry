package fun.bookish.blueberry.server.openapi.service.impl;

import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.hook.HttpHookExecutor;
import fun.bookish.blueberry.server.openapi.entity.OpenDeviceStatusVO;
import fun.bookish.blueberry.server.openapi.entity.OpenDeviceVO;
import fun.bookish.blueberry.server.openapi.service.IOpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * open api 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Service
public class OpenApiServiceImpl implements IOpenApiService {

    @Autowired
    private IChannelService channelService;
    @Autowired
    private IDeviceService deviceService;

    @Override
    public List<OpenDeviceStatusVO> queryDeviceStatusList() {
        return channelService
                .query()
                .isNotNull("parent_id")
                .list()
                .stream()
                .map(c -> {
                    OpenDeviceStatusVO openDeviceStatusVO = new OpenDeviceStatusVO();
                    openDeviceStatusVO.setId(c.getId());
                    openDeviceStatusVO.setOnline(c.getStatus());
                    return openDeviceStatusVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OpenDeviceStatusVO queryDeviceStatus(String id) {
        ChannelPO channelPO = channelService.query().eq("id", id).one();
        if (channelPO != null) {
            OpenDeviceStatusVO openDeviceStatusVO = new OpenDeviceStatusVO();
            openDeviceStatusVO.setId(channelPO.getId());
            openDeviceStatusVO.setOnline(channelPO.getStatus());
            return openDeviceStatusVO;
        }
        return null;
    }

    @Override
    public List<OpenDeviceVO> queryDeviceList() {
        Map<String, DevicePO> devicePOMap = deviceService.query().list().stream().collect(Collectors.toMap(DevicePO::getId, v -> v));
        return channelService
                .query()
                .isNotNull("parent_id")
                .list()
                .stream()
                .map(c -> OpenDeviceVO.create(c, devicePOMap.get(c.getDeviceId())))
                .collect(Collectors.toList());
    }

}
