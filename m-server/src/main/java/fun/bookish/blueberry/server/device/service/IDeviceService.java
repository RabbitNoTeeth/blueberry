package fun.bookish.blueberry.server.device.service;

import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.bookish.blueberry.server.device.entity.DeviceParamVO;
import fun.bookish.blueberry.server.device.entity.DeviceVO;

import java.util.List;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IDeviceService extends IService<DevicePO> {

    /**
     * 添加
     *
     * @param deviceVO
     * @return
     */
    String add(DeviceVO deviceVO);

    /**
     * 更新
     *
     * @param deviceVO
     * @return
     */
    String update(DeviceVO deviceVO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    String deleteById(String id);

    /**
     * 查询
     *
     * @return
     */
    DevicePO queryById(String id);

    /**
     * 查询列表
     *
     * @return
     * @param params
     */
    List<DeviceVO> queryList(DeviceParamVO params);

    /**
     * 新增或更新
     *
     * @return
     */
    String addOrUpdate(DevicePO devicePO);

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param params
     * @return
     */
    PageResult<DeviceVO> queryPage(Integer page, Integer pageSize, DeviceParamVO params);
}
