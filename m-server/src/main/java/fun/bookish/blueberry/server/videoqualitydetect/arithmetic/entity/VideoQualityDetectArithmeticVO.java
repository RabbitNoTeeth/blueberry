package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "VideoQualityDetectArithmetic实体对象", description = "视频质量检测算法实体")
public class VideoQualityDetectArithmeticVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", accessMode = ApiModelProperty.AccessMode.READ_ONLY, example = "1")
    private Integer id;

    @NotBlank(message = "编码不能为空")
    @ApiModelProperty(value = "编码", required = true)
    private String code;

    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "参数设置")
    private String settings;

    @NotNull(message = "优先级不能为空")
    @ApiModelProperty(value = "优先级", required = true, example = "1")
    private Integer priority;

    @ApiModelProperty(value = "是否启用（0：否，1：是）", required = true, example = "1")
    private Integer enable;

    @NotNull(message = "是否应用到所有设备不能为空")
    @ApiModelProperty(value = "是否应用到所有设备（0：否，1：是）", required = true, example = "1")
    private Integer applyAll;

    @ApiModelProperty(value = "创建时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String createdAt;

    @ApiModelProperty(value = "更新时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String updatedAt;

    @ApiModelProperty(value = "应用设备")
    private String applyDevices;


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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getApplyDevices() {
        return applyDevices;
    }

    public void setApplyDevices(String applyDevices) {
        this.applyDevices = applyDevices;
    }

    @Override
    public String toString() {
        return "VideoQualityDetectArithmeticVO{" +
                "id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", settings=" + settings +
                ", priority=" + priority +
                ", enable=" + enable +
                ", applyAll=" + applyAll +
                ", applyDevices=" + applyDevices +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }

}
