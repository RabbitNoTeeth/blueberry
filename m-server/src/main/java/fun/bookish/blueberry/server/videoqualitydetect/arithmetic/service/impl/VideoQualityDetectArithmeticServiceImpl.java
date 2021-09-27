package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.core.utils.BeanUtils;
import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.schedule.conf.VideoQualityDetectProperties;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.*;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.mapper.VideoQualityDetectArithmeticMapper;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.bookish.blueberry.server.videoqualitydetect.arithmeticapplydevice.entity.VideoQualityDetectArithmeticApplyDevicePO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmeticapplydevice.service.IVideoQualityDetectArithmeticApplyDeviceService;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频质量检测算法 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-10
 */
@Service
public class VideoQualityDetectArithmeticServiceImpl extends ServiceImpl<VideoQualityDetectArithmeticMapper, VideoQualityDetectArithmeticPO> implements IVideoQualityDetectArithmeticService {

    @Autowired
    private VideoQualityDetectProperties videoQualityDetectProperties;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IVideoQualityDetectArithmeticApplyDeviceService videoQualityDetectArithmeticApplyDeviceService;
    @Autowired
    private IChannelService channelService;

    /**
     * 添加
     *
     * @param videoQualityDetectArithmeticVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO) {
        VideoQualityDetectArithmeticPO arithmeticPO = BeanUtils.convert(videoQualityDetectArithmeticVO, VideoQualityDetectArithmeticPO.class);
        arithmeticPO.setEnable(0);
        String now = DateUtils.getNowDateTimeStr();
        arithmeticPO.setCreatedAt(now);
        arithmeticPO.setUpdatedAt(now);
        save(arithmeticPO);
        Integer applyAll = arithmeticPO.getApplyAll();
        String applyDevices = videoQualityDetectArithmeticVO.getApplyDevices();
        if (applyAll == 0 && StringUtils.isNotBlank(applyDevices)) {
            List<VideoQualityDetectArithmeticApplyDevicePO> applyDevicePOS = Arrays.stream(applyDevices.split(","))
                    .map(applyDevice -> {
                        String[] split = applyDevices.split("@");
                        VideoQualityDetectArithmeticApplyDevicePO applyDevicePO = new VideoQualityDetectArithmeticApplyDevicePO();
                        applyDevicePO.setArithmeticId(arithmeticPO.getId());
                        applyDevicePO.setDeviceId(split[0]);
                        applyDevicePO.setChannelId(split[1]);
                        applyDevicePO.setCreatedAt(DateUtils.getNowDateTimeStr());
                        return applyDevicePO;
                    })
                    .collect(Collectors.toList());
            videoQualityDetectArithmeticApplyDeviceService.saveBatch(applyDevicePOS);
        }
        return arithmeticPO.getId();
    }

    /**
     * 更新
     *
     * @param videoQualityDetectArithmeticVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO) {
        VideoQualityDetectArithmeticPO arithmeticPO = BeanUtils.convert(videoQualityDetectArithmeticVO, VideoQualityDetectArithmeticPO.class);
        Integer arithmeticId = arithmeticPO.getId();
        String now = DateUtils.getNowDateTimeStr();
        arithmeticPO.setCreatedAt(null);
        arithmeticPO.setUpdatedAt(now);
        updateById(arithmeticPO);
        QueryWrapper<VideoQualityDetectArithmeticApplyDevicePO> removeQuery = new QueryWrapper<>();
        removeQuery.eq("arithmetic_id", arithmeticId);
        videoQualityDetectArithmeticApplyDeviceService.remove(removeQuery);
        arithmeticPO = query().eq("id", arithmeticId).one();
        Integer applyAll = arithmeticPO.getApplyAll();
        String applyDevices = videoQualityDetectArithmeticVO.getApplyDevices();
        if (applyAll == 0 && StringUtils.isNotBlank(applyDevices)) {
            List<VideoQualityDetectArithmeticApplyDevicePO> applyDevicePOS = Arrays.stream(applyDevices.split(","))
                    .map(applyDevice -> {
                        String[] split = applyDevices.split("@");
                        VideoQualityDetectArithmeticApplyDevicePO applyDevicePO = new VideoQualityDetectArithmeticApplyDevicePO();
                        applyDevicePO.setArithmeticId(arithmeticId);
                        applyDevicePO.setDeviceId(split[0]);
                        applyDevicePO.setChannelId(split[1]);
                        applyDevicePO.setCreatedAt(DateUtils.getNowDateTimeStr());
                        return applyDevicePO;
                    })
                    .collect(Collectors.toList());
            videoQualityDetectArithmeticApplyDeviceService.saveBatch(applyDevicePOS);
        }
        return arithmeticId;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(Integer id) {
        removeById(id);
        // 删除 t_video_quality_detect_arithmetic_apply_device 中的关联数据
        QueryWrapper<VideoQualityDetectArithmeticApplyDevicePO> removeQuery = new QueryWrapper<>();
        removeQuery.eq("arithmetic_id", id);
        videoQualityDetectArithmeticApplyDeviceService.remove(removeQuery);
        return id;
    }

    /**
     * 查询列表
     *
     * @return
     * @param params
     */
    @Override
    public List<VideoQualityDetectArithmeticVO> queryList(VideoQualityDetectArithmeticQueryParamVO params) {
        QueryChainWrapper<VideoQualityDetectArithmeticPO> query = query();
        String name = params.getName();
        Integer enable = params.getEnable();
        Integer applyAll = params.getApplyAll();
        if (StringUtils.isNotBlank(name)) {
            query.like("name", name);
        }
        if (enable != null) {
            query.eq("enable", enable);
        }
        if (applyAll != null) {
            query.eq("apply_all", applyAll);
        }
        List<VideoQualityDetectArithmeticPO> list = query.orderByAsc("priority").list();
        return BeanUtils.convert(list, VideoQualityDetectArithmeticVO.class);
    }

    @Override
    public PageResult<VideoQualityDetectArithmeticVO> queryPage(Integer page, Integer pageSize, VideoQualityDetectArithmeticQueryParamVO params) {
        if (page < 1) {
            throw new ManualRollbackException("page参数值非法，必须大于等于1");
        } else {
            String name = params.getName();
            Integer enable = params.getEnable();
            Integer applyAll = params.getApplyAll();
            QueryWrapper<VideoQualityDetectArithmeticPO> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            if (enable != null) {
                queryWrapper.eq("enable", enable);
            }
            if (applyAll != null) {
                queryWrapper.eq("apply_all", applyAll);
            }
            Page<VideoQualityDetectArithmeticPO> pageEntity = new Page<>();
            pageEntity.setCurrent(page);
            pageEntity.setSize(pageSize);
            pageEntity.addOrder(new OrderItem("priority", true));
            Page<VideoQualityDetectArithmeticPO> res = page(pageEntity, queryWrapper);
            PageResult<VideoQualityDetectArithmeticVO> result = new PageResult<>(res.getTotal(), BeanUtils.convert(res.getRecords(), VideoQualityDetectArithmeticVO.class));
            return result;
        }
    }

    /**
     * 查询支持的算法列表
     *
     * @return
     */
    @Override
    public List<String> querySupportedArithmeticList() {
        List<VideoQualityDetectArithmeticPO> list = query().list();
        return videoQualityDetectProperties
                .getSupportedArithmetics()
                .stream()
                .filter(arithmetic -> list.stream().noneMatch(arithmeticInDb -> arithmeticInDb.getCode().equals(arithmetic.split(":")[0])))
                .collect(Collectors.toList());
    }

    /**
     * 查询可应用设备列表
     *
     * @return
     */
    @Override
    public List<VideoQualityDetectArithmeticApplicableDeviceVO> queryApplicableDeviceList() {
        List<VideoQualityDetectArithmeticApplicableDeviceVO> res = new ArrayList<>();
        VideoQualityDetectArithmeticApplicableDeviceVO root = new VideoQualityDetectArithmeticApplicableDeviceVO();
        root.setCode("root");
        root.setName("设备列表");
        List<VideoQualityDetectArithmeticApplicableDeviceVO> deviceNodes = deviceService.query().list()
                .stream()
                .map(device -> {
                    VideoQualityDetectArithmeticApplicableDeviceVO deviceNode = new VideoQualityDetectArithmeticApplicableDeviceVO();
                    deviceNode.setCode(device.getId());
                    deviceNode.setName(device.getId() + " " + device.getName());
                    return deviceNode;
                })
                .peek(device -> {
                    String deviceId = device.getCode();
                    List<VideoQualityDetectArithmeticApplicableDeviceVO> channels = channelService
                            .query()
                            .eq("device_id", deviceId)
                            .eq("parental", 0)
                            .list()
                            .stream()
                            .map(channel -> {
                                VideoQualityDetectArithmeticApplicableDeviceVO channelNode = new VideoQualityDetectArithmeticApplicableDeviceVO();
                                channelNode.setCode(deviceId + "@" + channel.getId());
                                channelNode.setName(channel.getId() + " " + channel.getName());
                                return channelNode;
                            })
                            .collect(Collectors.toList());
                    device.setChildren(channels);
                })
                .collect(Collectors.toList());
        root.setChildren(deviceNodes);
        res.add(root);
        return res;
    }

    /**
     * 查询已应用设备列表
     *
     * @param id
     * @return
     */
    @Override
    public List<String> queryAppliedDeviceList(Integer id) {

        if (id == null) {
            return new ArrayList<>();
        }
        return videoQualityDetectArithmeticApplyDeviceService
                .query()
                .eq("arithmetic_id", id)
                .list()
                .stream()
                .map(i -> i.getDeviceId() + "@" + i.getChannelId())
                .collect(Collectors.toList());
    }

}
