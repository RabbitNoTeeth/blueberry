package fun.bookish.blueberry.server.videoqualitydetect.record.mapper;

import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 视频质量检测记录表 Mapper 接口
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Mapper
public interface VideoQualityDetectRecordMapper extends BaseMapper<VideoQualityDetectRecordPO> {

}
