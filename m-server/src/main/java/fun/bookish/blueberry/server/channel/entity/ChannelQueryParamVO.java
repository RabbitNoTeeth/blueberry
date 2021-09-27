package fun.bookish.blueberry.server.channel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 设备通道表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_channel")
public class ChannelQueryParamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "通道ID", required = true)
    private String id;

    @ApiModelProperty(value = "设备编码", required = true)
    private String deviceId;

    @ApiModelProperty(value = "通道名称", required = true)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
