package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.controller;


import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticApplicableDeviceVO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticApplyDeviceVO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticVO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 视频质量检测算法 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-10
 */
@Api(tags = "视频质量检测算法管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/video-quality-detect/arithmetic")
public class VideoQualityDetectArithmeticController {

    @Autowired
    private IVideoQualityDetectArithmeticService videoQualityDetectArithmeticService;

    @ApiOperation("添加")
    @PostMapping("/add")
    public Integer add(@Valid VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO) {
        return videoQualityDetectArithmeticService.add(videoQualityDetectArithmeticVO);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public Integer update(VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO) {
        return videoQualityDetectArithmeticService.update(videoQualityDetectArithmeticVO);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public Integer delete(@NotNull(message = "id不能为空") Integer id) {
        return videoQualityDetectArithmeticService.deleteById(id);
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public List<VideoQualityDetectArithmeticVO> queryList(VideoQualityDetectArithmeticQueryParamVO params) {
        return videoQualityDetectArithmeticService.queryList(params);
    }

    @ApiOperation("分页查询列表")
    @GetMapping("/page")
    public PageResult<VideoQualityDetectArithmeticVO> queryPage(@NotNull(message = "page不能为空") Integer page,
                                                            @NotNull(message = "pageSize不能为空") Integer pageSize,
                                                            VideoQualityDetectArithmeticQueryParamVO params) {
        return videoQualityDetectArithmeticService.queryPage(page, pageSize, params);
    }

    @ApiOperation("查询支持的算法列表")
    @GetMapping("/supportedArithmetics")
    public List<String> querySupportedArithmeticList() {
        return videoQualityDetectArithmeticService.querySupportedArithmeticList();
    }

    @ApiOperation("查询可应用的设备列表")
    @GetMapping("/applicableDeviceList")
    public List<VideoQualityDetectArithmeticApplicableDeviceVO> queryApplicableDeviceList() {
        return videoQualityDetectArithmeticService.queryApplicableDeviceList();
    }

    @ApiOperation("查询已应用的设备列表")
    @GetMapping("/appliedDeviceList")
    public List<String> queryAppliedDeviceList(Integer id) {
        return videoQualityDetectArithmeticService.queryAppliedDeviceList(id);
    }

}

