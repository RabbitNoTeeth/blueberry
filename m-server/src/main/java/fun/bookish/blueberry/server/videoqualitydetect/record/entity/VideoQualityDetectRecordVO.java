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
@ApiModel(value = "VideoQualityDetectRecord实体对象", description = "视频质量检测记录实体")
public class VideoQualityDetectRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", accessMode = ApiModelProperty.AccessMode.READ_ONLY, example = "1")
    private Integer id;

    @ApiModelProperty(value = "算法编码")
    private String arithmeticCode;

    @ApiModelProperty(value = "算法名称")
    private String arithmeticName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "通道ID")
    private String channelId;

    @ApiModelProperty(value = "图片位置")
    private String imagePath;

    @ApiModelProperty(value = "是否失败", example = "0")
    private Integer hasError;

    @ApiModelProperty(value = "失败信息")
    private String error;

    @ApiModelProperty(value = "是否异常", example = "0")
    private Integer hasQualityError;

    @ApiModelProperty(value = "异常信息")
    private String qualityError;

    @ApiModelProperty(value = "检测详情")
    private String detail;

    @ApiModelProperty(value = "创建时间")
    private String createdAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getHasError() {
        return hasError;
    }

    public void setHasError(Integer hasError) {
        this.hasError = hasError;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getHasQualityError() {
        return hasQualityError;
    }

    public void setHasQualityError(Integer hasQualityError) {
        this.hasQualityError = hasQualityError;
    }

    public String getQualityError() {
        return qualityError;
    }

    public void setQualityError(String qualityError) {
        this.qualityError = qualityError;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getArithmeticCode() {
        return arithmeticCode;
    }

    public void setArithmeticCode(String arithmeticCode) {
        this.arithmeticCode = arithmeticCode;
    }

    public String getArithmeticName() {
        return arithmeticName;
    }

    public void setArithmeticName(String arithmeticName) {
        this.arithmeticName = arithmeticName;
    }

    @Override
    public String toString() {
        return "VidoeQualityDetectRecordVO{" +
        "id=" + id +
        ", arithmeticCode=" + arithmeticCode +
        ", arithmeticName=" + arithmeticName +
        ", deviceId=" + deviceId +
        ", channelId=" + channelId +
        ", imagePath=" + imagePath +
        ", hasError=" + hasError +
        ", error=" + error +
        ", hasQualityError=" + hasQualityError +
        ", qualityError=" + qualityError +
        ", detail=" + detail +
        ", createdAt=" + createdAt +
        "}";
    }
}
