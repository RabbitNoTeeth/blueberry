package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频质量检测算法
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-10
 */
@TableName("t_video_quality_detect_arithmetic")
public class VideoQualityDetectArithmeticPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 策略设置
     */
    private String settings;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否启用（0：否，1：是）
     */
    private Integer enable;

    /**
     * 是否应用到所有设备（0：否，1：是）
     */
    private Integer applyAll;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 更新时间
     */
    private String updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getApplyAll() {
        return applyAll;
    }

    public void setApplyAll(Integer applyAll) {
        this.applyAll = applyAll;
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
        return "VideoQualityDetectArithmeticPO{" +
        "id=" + id +
        ", code=" + code +
        ", name=" + name +
        ", settings=" + settings +
        ", enable=" + enable +
        ", applyAll=" + applyAll +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
