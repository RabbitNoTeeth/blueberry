package fun.bookish.blueberry.server.openapi.service;

import fun.bookish.blueberry.server.openapi.entity.OpenDeviceStatusVO;
import fun.bookish.blueberry.server.openapi.entity.OpenDeviceVO;

import java.util.List;

/**
 * <p>
 * open api 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IOpenApiService {

    List<OpenDeviceStatusVO> queryDeviceStatusList();

    OpenDeviceStatusVO queryDeviceStatus(String id);

    List<OpenDeviceVO> queryDeviceList();
}
