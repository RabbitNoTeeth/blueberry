package fun.bookish.blueberry.server.channel.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.core.utils.BeanUtils;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.entity.ChannelQueryParamVO;
import fun.bookish.blueberry.server.channel.entity.ChannelVO;
import fun.bookish.blueberry.server.channel.mapper.ChannelMapper;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.hook.HttpHookExecutor;
import fun.bookish.blueberry.server.sip.conf.SipProperties;
import fun.bookish.blueberry.sip.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 设备通道表 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, ChannelPO> implements IChannelService {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private HttpHookExecutor httpHookExecutor;
    @Autowired
    private SipProperties sipProperties;
    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public boolean save(ChannelPO entity) {
        boolean success = super.save(entity);
        // 触发http hook
        if (success) {
            String deviceId = entity.getDeviceId();
            HttpHookExecutor.ChannelEntity channel = HttpHookExecutor.ChannelEntity.mapFromChannelPO(queryByIdAndDeviceId(entity.getId(), deviceId));
            HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(deviceService.queryById(deviceId));
            device.setServerIp(sipProperties.getHost());
            device.setServerPort(serverPort);
            channel.setDevice(device);
            httpHookExecutor.onChannelCreated(channel);
        }
        return success;
    }

    @Override
    public boolean update(ChannelPO entity, Wrapper<ChannelPO> updateWrapper) {
        boolean success = super.update(entity, updateWrapper);
        // 触发http hook
        if (success) {
            String deviceId = entity.getDeviceId();
            HttpHookExecutor.ChannelEntity channel = HttpHookExecutor.ChannelEntity.mapFromChannelPO(queryByIdAndDeviceId(entity.getId(), deviceId));
            HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(deviceService.queryById(deviceId));
            device.setServerIp(sipProperties.getHost());
            device.setServerPort(serverPort);
            channel.setDevice(device);
            httpHookExecutor.onChannelUpdated(channel);
        }
        return success;
    }

    @Override
    public boolean remove(Wrapper<ChannelPO> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    public String add(ChannelVO channelVO) {
        ChannelPO entity = BeanUtils.convert(channelVO, ChannelPO.class);
        entity.setParental(0);
        entity.setStatus("OFF");
        String now = DateUtils.getNowDateTimeStr();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        save(entity);
        return entity.getId();
    }

    @Override
    public String update(ChannelVO channelVO) {
        ChannelPO entity = BeanUtils.convert(channelVO, ChannelPO.class);
        String now = DateUtils.getNowDateTimeStr();
        entity.setUpdatedAt(now);
        UpdateWrapper<ChannelPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", entity.getId()).eq("device_id", entity.getDeviceId());
        update(entity, updateWrapper);
        return entity.getId();
    }

    @Override
    public String deleteByIdAndDeviceId(String id, String deviceId) {
        QueryWrapper<ChannelPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("device_id", deviceId);
        boolean success = remove(queryWrapper);
        // 触发http hook
        if (success) {
            HttpHookExecutor.ChannelRemove hookData = new HttpHookExecutor.ChannelRemove();
            hookData.setDeviceId(deviceId);
            hookData.setChannelId(id);
            httpHookExecutor.onChannelRemoved(hookData);
        }
        return id;
    }

    @Override
    public List<ChannelVO> queryList(ChannelQueryParamVO params) {
        QueryChainWrapper<ChannelPO> query = query();
        String id =  params.getId();
        String deviceId = params.getDeviceId();
        String name = params.getName();
        if (StringUtils.isNotBlank(id)) {
            query.like("id", id);
        }
        if (StringUtils.isNotBlank(deviceId)) {
            query.eq("device_id", deviceId);
        }
        if (StringUtils.isNotBlank(name)) {
            query.like("name", name);
        }
        List<ChannelPO> channelPOS = query.list();
        return BeanUtils.convert(channelPOS, ChannelVO.class);
    }

    @Override
    public PageResult<ChannelVO> queryPage(Integer page, Integer pageSize, ChannelQueryParamVO params) {
        if (page < 1) {
            throw new ManualRollbackException("page参数值非法，必须大于等于1");
        } else {
            QueryWrapper<ChannelPO> query = new QueryWrapper<>();
            String id =  params.getId();
            String deviceId = params.getDeviceId();
            String name = params.getName();
            if (StringUtils.isNotBlank(id)) {
                query.like("id", id);
            }
            if (StringUtils.isNotBlank(deviceId)) {
                query.eq("device_id", deviceId);
            }
            if (StringUtils.isNotBlank(name)) {
                query.like("name", name);
            }
            Page<ChannelPO> pageEntity = new Page<>();
            pageEntity.setCurrent(page);
            pageEntity.setSize(pageSize);
            Page<ChannelPO> res = page(pageEntity, query);
            PageResult<ChannelVO> result = new PageResult<>(res.getTotal(), BeanUtils.convert(res.getRecords(), ChannelVO.class));
            return result;
        }
    }

    @Override
    public ChannelPO queryByIdAndDeviceId(String id, String deviceId) {
        return query().eq("id", id).eq("device_id", deviceId).one();
    }

    @Override
    public String deleteByDeviceId(String deviceId) {
        List<ChannelPO> channels = query().eq("device_id", deviceId).list();
        if (channels.size() != 0) {
            channels.forEach(channel -> deleteByIdAndDeviceId(channel.getId(), deviceId));
        }
        return deviceId;
    }

    @Override
    public synchronized String addOrUpdate(ChannelPO channelPO) {
        String id = channelPO.getId();
        ChannelPO channel = query().eq("id", channelPO.getId()).eq("device_id", channelPO.getDeviceId()).one();
        if (channel != null) {
            UpdateWrapper<ChannelPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", channelPO.getId()).eq("device_id", channelPO.getDeviceId());
            update(channelPO, updateWrapper);
        } else {
            save(channelPO);
        }
        return id;
    }

    @Override
    public void addOrUpdate(List<ChannelPO> channels) throws Exception {
        if (channels.size() == 0) {
            return;
        }
        String deviceId = channels.get(0).getDeviceId();
        DevicePO devicePO = deviceService.queryById(deviceId);
        int i = 1;
        while (devicePO == null) {
            if (i == 10) {
                throw new ManualRollbackException("设备通道保存失败，原因：数据库中未找到通道所属设备信息");
            }
            Thread.sleep(1000);
            devicePO = deviceService.queryById(deviceId);
            i++;
        }
        channels.forEach(this::addOrUpdate);
    }

}
