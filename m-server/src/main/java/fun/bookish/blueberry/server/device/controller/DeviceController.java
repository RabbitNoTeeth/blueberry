package fun.bookish.blueberry.server.device.controller;


import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.device.entity.DeviceParamVO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticQueryParamVO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticVO;
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
 * 设备表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "设备管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/device")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @ApiOperation("添加")
    @PostMapping("/add")
    public String add(DeviceVO deviceVO) {
        return deviceService.add(deviceVO);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public String update(DeviceVO deviceVO) {
        return deviceService.update(deviceVO);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public String delete(@NotBlank(message = "id不能为空") String id) {
        return deviceService.deleteById(id);
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public List<DeviceVO> queryList(DeviceParamVO params) {
        return deviceService.queryList(params);
    }

    @ApiOperation("分页查询列表")
    @GetMapping("/page")
    public PageResult<DeviceVO> queryPage(@NotNull(message = "page不能为空") Integer page,
                                          @NotNull(message = "pageSize不能为空") Integer pageSize,
                                          DeviceParamVO params) {
        return deviceService.queryPage(page, pageSize, params);
    }

}

