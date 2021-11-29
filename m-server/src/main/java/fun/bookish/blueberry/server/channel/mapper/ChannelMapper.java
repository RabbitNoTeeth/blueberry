package fun.bookish.blueberry.server.channel.mapper;

import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.bookish.blueberry.server.channel.entity.ChannelStatusSync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 设备通道表 Mapper 接口
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Component
@Mapper
public interface ChannelMapper extends BaseMapper<ChannelPO> {

    @Select("select\n" +
            "            a.id as id,\n" +
            "            a.device_id as device_id,\n" +
            "            concat(b.remote_ip,':',b.remote_port) as address,\n" +
            "            b.command_transport as transport\n" +
            "        from\n" +
            "            t_channel a\n" +
            "        left join\n" +
            "            t_device b on b.id = a.device_id")
    List<ChannelStatusSync> queryChannelStatusSyncList();

}
