package fun.bookish.blueberry.server.channel.service;

import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.bookish.blueberry.server.channel.entity.ChannelQueryParamVO;
import fun.bookish.blueberry.server.channel.entity.ChannelStatusSync;
import fun.bookish.blueberry.server.channel.entity.ChannelVO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;

import java.util.List;

/**
 * <p>
 * 设备通道表 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IChannelService extends IService<ChannelPO> {

    String add(ChannelVO channelVO);

    String update(ChannelVO channelVO);

    String deleteByIdAndDeviceId(String id, String deviceId);

    List<ChannelVO> queryList(ChannelQueryParamVO params);

    PageResult<ChannelVO> queryPage(Integer page, Integer pageSize, ChannelQueryParamVO params);

    ChannelPO queryByIdAndDeviceId(String id, String deviceId);

    String deleteByDeviceId(String deviceId);

    String addOrUpdate(ChannelPO channelPO);

    void addOrUpdate(List<ChannelPO> channels) throws Exception;

    List<ChannelStatusSync> queryChannelStatusSyncList();

}
