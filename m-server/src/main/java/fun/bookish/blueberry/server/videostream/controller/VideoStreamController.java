package fun.bookish.blueberry.server.videostream.controller;


import fun.bookish.blueberry.core.annotation.DisableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.utils.Base64Utils;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamPlayResponse;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamStopResponse;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamVO;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import fun.bookish.blueberry.server.zlmedia.connect.ZLMediaKitConnection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 视频流表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "视频管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/video-stream")
public class VideoStreamController {

    @Autowired
    private IVideoStreamService videoStreamService;
    @Autowired
    private ZLMediaKitConnection zlMediaKitConnection;

    @ApiOperation("查询视频流列表")
    @GetMapping("/list")
    public List<VideoStreamVO> queryList() throws Exception {
        return videoStreamService.queryList();
    }

    @ApiOperation("查询视频流截图")
    @GetMapping(value = "/snap/{param}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @DisableResponseBodyJsonWrap
    public byte[] snap(@PathVariable String param) throws Exception {
        return zlMediaKitConnection.querySnap(Base64Utils.decode2string(param));
    }

    @ApiOperation("播放视频流")
    @GetMapping("/play")
    public VideoStreamPlayResponse play(@NotBlank(message = "设备ID不能为空") String deviceId,
                                        @NotBlank(message = "通道ID不能为空") String channelId) throws Exception {
        return videoStreamService.play(deviceId, channelId);
    }

    @ApiOperation("停止视频流")
    @GetMapping("/stop")
    public VideoStreamStopResponse stop(@NotBlank(message = "streamId不能为空") String streamId) throws Exception {
        return videoStreamService.stop(streamId);
    }

}

