package fun.bookish.blueberry.server.channel.controller;


import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.channel.entity.ChannelQueryParamVO;
import fun.bookish.blueberry.server.channel.entity.ChannelVO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DeviceParamVO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 设备通道表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "设备通道管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private IChannelService channelService;

    @ApiOperation("添加")
    @PostMapping("/add")
    public String add(ChannelVO channelVO) {
        return channelService.add(channelVO);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public String update(ChannelVO channelVO) {
        return channelService.update(channelVO);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public String delete(@NotBlank(message = "id不能为空") String id, @NotBlank(message = "deviceId不能为空") String deviceId) {
        return channelService.deleteByIdAndDeviceId(id, deviceId);
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public List<ChannelVO> queryList(ChannelQueryParamVO params) {
        return channelService.queryList(params);
    }

    @ApiOperation("分页查询列表")
    @GetMapping("/page")
    public PageResult<ChannelVO> queryPage(@NotNull(message = "page不能为空") Integer page,
                                          @NotNull(message = "pageSize不能为空") Integer pageSize,
                                          ChannelQueryParamVO params) {
        return channelService.queryPage(page, pageSize, params);
    }
}

