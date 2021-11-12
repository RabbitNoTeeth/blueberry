package fun.bookish.blueberry.server.openapi.controller;


import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.server.openapi.entity.OpenDeviceStatusVO;
import fun.bookish.blueberry.server.openapi.entity.OpenDeviceVO;
import fun.bookish.blueberry.server.openapi.service.IOpenApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * open api 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "OpenAPI管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/open")
public class OpenApiController {

    @Autowired
    private IOpenApiService openApiService;

    @ApiOperation("查询设备列表")
    @GetMapping("/device/list")
    public List<OpenDeviceVO> deviceList() {
        return openApiService.queryDeviceList();
    }

    @ApiOperation("查询设备状态列表")
    @GetMapping("/device/status/list")
    public List<OpenDeviceStatusVO> deviceStatusList() {
        return openApiService.queryDeviceStatusList();
    }

    @ApiOperation("查询设备状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, type = "query")
    })
    @GetMapping("/device/status")
    public OpenDeviceStatusVO deviceStatus(@ApiIgnore @NotBlank(message = "id不能为空") String id) {
        return openApiService.queryDeviceStatus(id);
    }

}

