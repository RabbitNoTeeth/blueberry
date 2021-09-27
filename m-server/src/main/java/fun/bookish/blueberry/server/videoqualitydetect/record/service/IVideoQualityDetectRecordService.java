package fun.bookish.blueberry.server.videoqualitydetect.record.service;

import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;

import java.util.List;

/**
 * <p>
 * 视频质量检测记录表 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IVideoQualityDetectRecordService extends IService<VideoQualityDetectRecordPO> {

    /**
     * 查询列表
     *
     * @return
     * @param params
     */
    List<VideoQualityDetectRecordVO> queryList(VideoQualityDetectRecordQueryParamVO params);

    /**
     * 查询视频快照
     *
     * @param id
     * @return
     */
    byte[] querySnap(Integer id) throws Exception;

    /**
     * 查询视频快照
     *
     * @param path
     * @return
     */
    byte[] querySnapByPath(String path) throws Exception;

    /**
     * 分页查询列表
     *
     * @param page
     * @param pageSize
     * @param params
     * @return
     */
    PageResult<VideoQualityDetectRecordVO> queryPage(Integer page, Integer pageSize, VideoQualityDetectRecordQueryParamVO params);
}
