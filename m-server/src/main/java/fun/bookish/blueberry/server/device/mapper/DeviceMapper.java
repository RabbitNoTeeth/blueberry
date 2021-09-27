package fun.bookish.blueberry.server.device.mapper;

import fun.bookish.blueberry.server.device.entity.DevicePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 设备表 Mapper 接口
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Mapper
public interface DeviceMapper extends BaseMapper<DevicePO> {

}
