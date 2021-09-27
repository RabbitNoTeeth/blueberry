package fun.bookish.blueberry.server.videoqualitydetect.record.controller;

import fun.bookish.blueberry.core.annotation.DisableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.service.IVideoQualityDetectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 视频质量检测记录表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "视频质量检测记录管理")
@EnableResponseBodyJsonWrap
@RestController
@RequestMapping("/api/v1/video-quality-detect/arithmetic/record")
public class VideoQualityDetectRecordController {

    @Autowired
    private IVideoQualityDetectRecordService videoQualityDetectRecordService;

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public List<VideoQualityDetectRecordVO> queryList(VideoQualityDetectRecordQueryParamVO params) {
        return videoQualityDetectRecordService.queryList(params);
    }

    @ApiOperation("分页查询列表")
    @GetMapping("/page")
    public PageResult<VideoQualityDetectRecordVO> queryPage(@NotNull(message = "page不能为空") Integer page,
                                                            @NotNull(message = "pageSize不能为空") Integer pageSize,
                                                            VideoQualityDetectRecordQueryParamVO params) {
        return videoQualityDetectRecordService.queryPage(page, pageSize, params);
    }

    @ApiOperation("查询视频快照")
    @GetMapping(value = "/snap", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @DisableResponseBodyJsonWrap
    public byte[] snap(@NotNull(message = "id不能为空") Integer id) throws Exception {
        return videoQualityDetectRecordService.querySnap(id);
    }

    @ApiOperation("查询视频快照")
    @GetMapping(value = "/snapByPath", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @DisableResponseBodyJsonWrap
    public byte[] snapByPath(@NotBlank(message = "path不能为空") String path) throws Exception {
        return videoQualityDetectRecordService.querySnapByPath(path);
    }

}

