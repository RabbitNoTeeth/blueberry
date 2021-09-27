package fun.bookish.blueberry.server.videoqualitydetect.manual.controller;

import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.server.videoqualitydetect.manual.service.IVideoQualityDetectManualService;
import fun.bookish.blueberry.server.videoqualitydetect.manual.entity.VideoQualityDetectManualVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * 视频质量检测记录表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "手动图像检测管理")
@EnableResponseBodyJsonWrap
@RestController
@RequestMapping("/api/v1/video-quality-detect/manual")
public class VideoQualityDetectManualController {

    @Autowired
    private IVideoQualityDetectManualService videoQualityDetectManualService;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public VideoQualityDetectManualVO upload(@RequestParam("file") MultipartFile file) throws Exception {
        return videoQualityDetectManualService.upload(file);
    }

}

