package fun.bookish.blueberry.server.videostream.mapper;

import fun.bookish.blueberry.server.videostream.entity.VideoStreamPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 视频流表 Mapper 接口
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Mapper
public interface VideoStreamMapper extends BaseMapper<VideoStreamPO> {

}
