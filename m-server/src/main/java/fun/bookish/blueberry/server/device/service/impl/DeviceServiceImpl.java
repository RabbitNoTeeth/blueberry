package fun.bookish.blueberry.server.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.core.utils.BeanUtils;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.entity.DeviceParamVO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;
import fun.bookish.blueberry.server.device.mapper.DeviceMapper;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.bookish.blueberry.server.hook.HttpHookExecutor;
import fun.bookish.blueberry.server.sip.conf.SipProperties;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticPO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticVO;
import fun.bookish.blueberry.sip.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DevicePO> implements IDeviceService {

    @Autowired
    private IChannelService channelService;
    @Autowired
    private HttpHookExecutor httpHookExecutor;
    @Autowired
    private SipProperties sipProperties;
    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public boolean save(DevicePO entity) {
        boolean success = super.save(entity);
        // 触发http hook
        if (success) {
            HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(query().eq("id", entity.getId()).one());
            device.setServerIp(sipProperties.getHost());
            device.setServerPort(serverPort);
            httpHookExecutor.onDeviceCreated(device);
        }
        return success;
    }

    @Override
    public boolean updateById(DevicePO entity) {
        boolean success = super.updateById(entity);
        // 触发http hook
        if (success) {
            HttpHookExecutor.DeviceEntity device = HttpHookExecutor.DeviceEntity.mapFromDevicePO(queryById(entity.getId()));
            device.setServerIp(sipProperties.getHost());
            device.setServerPort(serverPort);
            httpHookExecutor.onDeviceUpdated(device);
        }
        return success;
    }

    @Override
    public String add(DeviceVO deviceVO) {
        DevicePO devicePO = BeanUtils.convert(deviceVO, DevicePO.class);
        devicePO.setOnline(0);
        String now = DateUtils.getNowDateTimeStr();
        devicePO.setCreatedAt(now);
        devicePO.setUpdatedAt(now);
        save(devicePO);
        return deviceVO.getId();
    }

    @Override
    public String update(DeviceVO deviceVO) {
        DevicePO devicePO = BeanUtils.convert(deviceVO, DevicePO.class);
        String now = DateUtils.getNowDateTimeStr();
        devicePO.setUpdatedAt(now);
        updateById(devicePO);
        return deviceVO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteById(String id) {
        boolean success = removeById(id);
        channelService.deleteByDeviceId(id);
        // 触发http hook
        if (success) {
            HttpHookExecutor.DeviceRemove hookData = new HttpHookExecutor.DeviceRemove();
            hookData.setDeviceId(id);
            httpHookExecutor.onDeviceRemoved(hookData);
        }
        return id;
    }

    @Override
    public DevicePO queryById(String id) {
        return query().eq("id", id).one();
    }

    @Override
    public List<DeviceVO> queryList(DeviceParamVO params) {
        QueryChainWrapper<DevicePO> query = query();
        String id = params.getId();
        String name = params.getName();
        String type = params.getType();
        Integer online = params.getOnline();
        if (StringUtils.isNotBlank(id)) {
            query.like("id", id);
        }
        if (StringUtils.isNotBlank(name)) {
            query.like("name", name);
        }
        if (StringUtils.isNotBlank(type)) {
            query.eq("type", type);
        }
        if (online != null) {
            query.eq("online", online);
        }
        List<DevicePO> list = query.orderByDesc("created_at").list();
        return BeanUtils.convert(list, DeviceVO.class);
    }

    @Override
    public PageResult<DeviceVO> queryPage(Integer page, Integer pageSize, DeviceParamVO params) {
        if (page < 1) {
            throw new ManualRollbackException("page参数值非法，必须大于等于1");
        } else {
            QueryWrapper<DevicePO> query = new QueryWrapper<>();
            String id = params.getId();
            String name = params.getName();
            String type = params.getType();
            Integer online = params.getOnline();
            if (StringUtils.isNotBlank(id)) {
                query.like("id", id);
            }
            if (StringUtils.isNotBlank(name)) {
                query.like("name", name);
            }
            if (StringUtils.isNotBlank(type)) {
                query.eq("type", type);
            }
            if (online != null) {
                query.eq("online", online);
            }
            Page<DevicePO> pageEntity = new Page<>();
            pageEntity.setCurrent(page);
            pageEntity.setSize(pageSize);
            Page<DevicePO> res = page(pageEntity, query);
            PageResult<DeviceVO> result = new PageResult<>(res.getTotal(), BeanUtils.convert(res.getRecords(), DeviceVO.class));
            return result;
        }
    }

    @Override
    public synchronized String addOrUpdate(DevicePO devicePO) {
        String id = devicePO.getId();
        DevicePO device = query().eq("id", devicePO.getId()).one();
        if (device != null) {
            updateById(devicePO);
        } else {
            save(devicePO);
        }
        return id;
    }

}
