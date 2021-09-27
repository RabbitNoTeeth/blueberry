package fun.bookish.blueberry.server.videoqualitydetect.manual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.videoqualitydetect.manual.entity.VideoQualityDetectManualVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 视频质量检测记录表 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IVideoQualityDetectManualService {

    /**
     * 图片上传
     *
     * @return
     * @param file
     */
    VideoQualityDetectManualVO upload(MultipartFile file) throws IOException, Exception;

}
