package fun.bookish.blueberry.server.videoqualitydetect.record.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 视频质量检测记录表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@ApiModel(value = "VideoQualityDetectRecord查询参数对象", description = "视频质量检测记录查询参数实体")
public class VideoQualityDetectRecordQueryParamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "算法名称")
    private String arithmeticName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "通道ID")
    private String channelId;

    @ApiModelProperty(value = "是否异常", example = "0")
    private Integer hasQualityError;

    public String getArithmeticName() {
        return arithmeticName;
    }

    public void setArithmeticName(String arithmeticName) {
        this.arithmeticName = arithmeticName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getHasQualityError() {
        return hasQualityError;
    }

    public void setHasQualityError(Integer hasQualityError) {
        this.hasQualityError = hasQualityError;
    }
}
