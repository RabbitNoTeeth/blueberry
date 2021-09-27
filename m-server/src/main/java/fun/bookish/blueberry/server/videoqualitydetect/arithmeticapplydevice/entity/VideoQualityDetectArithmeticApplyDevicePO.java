package fun.bookish.blueberry.server.videoqualitydetect.arithmeticapplydevice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 视频质量检测算法应用设备表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-15
 */
@TableName("t_video_quality_detect_arithmetic_apply_device")
public class VideoQualityDetectArithmeticApplyDevicePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 算法id
     */
    private Integer arithmeticId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 通道id
     */
    private String channelId;

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

    public Integer getArithmeticId() {
        return arithmeticId;
    }

    public void setArithmeticId(Integer arithmeticId) {
        this.arithmeticId = arithmeticId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "VideoQualityDetectArithmeticApplyDevicePO{" +
        "id=" + id +
        ", arithmeticId=" + arithmeticId +
        ", deviceId=" + deviceId +
        ", channelId=" + channelId +
        ", createdAt=" + createdAt +
        "}";
    }
}
