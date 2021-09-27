package fun.bookish.blueberry.server.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_device")
public class DeviceParamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备编码", required = true)
    private String id;

    @ApiModelProperty(value = "设备名称", required = true)
    private String name;

    @ApiModelProperty(value = "设备类型", required = true, example = "GB, RTSP")
    private String type;

    @ApiModelProperty(value = "是否在线（0：否，1：是）", required = true, example = "0")
    private Integer online;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

}
