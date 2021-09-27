package fun.bookish.blueberry.server.videostream.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 视频流表
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@TableName("t_video_stream")
public class VideoStreamPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 类型
     */
    private String type;

    /**
     * ssrc
     */
    private String ssrc;

    /**
     * flv地址
     */
    private String flv;

    /**
     * 停止参数
     */
    private String stopParams;

    /**
     * 连接状态（0：连接中，1：已连接）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 更新时间
     */
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSsrc() {
        return ssrc;
    }

    public void setSsrc(String ssrc) {
        this.ssrc = ssrc;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getStopParams() {
        return stopParams;
    }

    public void setStopParams(String stopParams) {
        this.stopParams = stopParams;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "VideoStreamPO{" +
        "id=" + id +
        ", deviceId=" + deviceId +
        ", channelId=" + channelId +
        ", type=" + type +
        ", ssrc=" + ssrc +
        ", flv=" + flv +
        ", stopParams=" + stopParams +
        ", status=" + status +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
