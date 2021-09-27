package fun.bookish.blueberry.server.videoqualitydetect.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.core.utils.BeanUtils;
import fun.bookish.blueberry.core.utils.FileUtils;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.mapper.VideoQualityDetectRecordMapper;
import fun.bookish.blueberry.server.videoqualitydetect.record.service.IVideoQualityDetectRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 视频质量检测记录表 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Service
public class VideoQualityDetectRecordServiceImpl extends ServiceImpl<VideoQualityDetectRecordMapper, VideoQualityDetectRecordPO> implements IVideoQualityDetectRecordService {

    /**
     * 查询列表
     *
     * @return
     * @param params
     */
    @Override
    public List<VideoQualityDetectRecordVO> queryList(VideoQualityDetectRecordQueryParamVO params) {
        QueryChainWrapper<VideoQualityDetectRecordPO> query = query();
        String arithmeticName = params.getArithmeticName();
        String deviceId = params.getDeviceId();
        String channelId = params.getChannelId();
        Integer hasQualityError = params.getHasQualityError();
        if (StringUtils.isNotBlank(arithmeticName)) {
            query.like("arithmetic_name", arithmeticName);
        }
        if (StringUtils.isNotBlank(deviceId)) {
            query.like("device_id", deviceId);
        }
        if (StringUtils.isNotBlank(channelId)) {
            query.like("channel_id", channelId);
        }
        if (hasQualityError != null) {
            query.eq("has_quality_error", hasQualityError);
        }
        List<VideoQualityDetectRecordPO> recordPOS = query.orderByDesc("created_at").list();
        return BeanUtils.convert(recordPOS, VideoQualityDetectRecordVO.class);
    }

    /**
     * 查询视频快照
     *
     * @param id
     * @return
     */
    @Override
    public byte[] querySnap(Integer id) throws Exception {
        VideoQualityDetectRecordPO recordPO = query().eq("id", id).one();
        if (recordPO == null) {
            return new byte[0];
        }
        return FileUtils.read(recordPO.getImagePath());
    }

    /**
     * 查询视频快照
     *
     * @param path
     * @return
     */
    @Override
    public byte[] querySnapByPath(String path) throws Exception {
        return FileUtils.read(path);
    }

    /**
     * 分页查询列表
     *
     * @param page
     * @param pageSize
     * @param params
     * @return
     */
    @Override
    public PageResult<VideoQualityDetectRecordVO> queryPage(Integer page, Integer pageSize, VideoQualityDetectRecordQueryParamVO params) {
        if (page < 1) {
            throw new ManualRollbackException("page参数值非法，必须大于等于1");
        } else {
            String arithmeticName = params.getArithmeticName();
            String deviceId = params.getDeviceId();
            String channelId = params.getChannelId();
            Integer hasQualityError = params.getHasQualityError();
            QueryWrapper<VideoQualityDetectRecordPO> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(arithmeticName)) {
                queryWrapper.like("arithmetic_name", arithmeticName);
            }
            if (StringUtils.isNotBlank(deviceId)) {
                queryWrapper.like("device_id", deviceId);
            }
            if (StringUtils.isNotBlank(channelId)) {
                queryWrapper.like("channel_id", channelId);
            }
            if (hasQualityError != null) {
                queryWrapper.eq("has_quality_error", hasQualityError);
            }
            queryWrapper.orderByDesc("created_at");
            Page<VideoQualityDetectRecordPO> pageEntity = new Page<>();
            pageEntity.setCurrent(page);
            pageEntity.setSize(pageSize);
            Page<VideoQualityDetectRecordPO> res = page(pageEntity, queryWrapper);
            PageResult<VideoQualityDetectRecordVO> result = new PageResult<>(res.getTotal(), BeanUtils.convert(res.getRecords(), VideoQualityDetectRecordVO.class));
            return result;
        }
    }

}
