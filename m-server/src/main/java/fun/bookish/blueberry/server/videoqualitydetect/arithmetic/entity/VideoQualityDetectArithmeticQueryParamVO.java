package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 视频质量检测算法
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-10
 */
@ApiModel(value = "VideoQualityDetectArithmetic实体对象", description = "视频质量检测算法实体")
public class VideoQualityDetectArithmeticQueryParamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "是否启用（0：否，1：是）", required = true, example = "1")
    private Integer enable;

    @ApiModelProperty(value = "是否应用到所有设备（0：否，1：是）", required = true, example = "1")
    private Integer applyAll;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
