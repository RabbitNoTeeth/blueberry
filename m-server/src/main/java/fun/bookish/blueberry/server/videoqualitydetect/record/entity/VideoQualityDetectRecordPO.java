package fun.bookish.blueberry.server.videoqualitydetect.record.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 视频质量检测记录表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_video_quality_detect_record")
public class VideoQualityDetectRecordPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 算法编码
     */
    private String arithmeticCode;
    /**
     * 算法名称
     */
    private String arithmeticName;
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 图片文件地址
     */
    private String imagePath;

    /**
     * 是否异常
     */
    private Integer hasError;

    /**
     * 异常信息
     */
    private String error;

    /**
     * 是否存在质量问题
     */
    private Integer hasQualityError;

    /**
     * 质量问题信息
     */
    private String qualityError;

    /**
     * 质量检测详情
     */
    private String detail;

    /**
     * 创建时间
     */
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

    @Override
    public String toString() {
        return "VidoeQualityDetectRecordPO{" +
        "id=" + id +
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
}
