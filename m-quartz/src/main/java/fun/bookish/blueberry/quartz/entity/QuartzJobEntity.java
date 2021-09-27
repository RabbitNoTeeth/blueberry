package fun.bookish.blueberry.quartz.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 定时任务实体
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public class QuartzJobEntity implements Comparable<QuartzJobEntity> {

    @ApiModelProperty(value = "任务名称", required = true)
    @NotBlank(message = "任务名称不能为空")
    @Length(max = 255, message = "任务名称长度不能超过255字符")
    private String name;

    @ApiModelProperty(value = "任务组名称", required = true)
    @NotBlank(message = "任务组名称不能为空")
    @Length(max = 255, message = "任务组名称长度不能超过255字符")
    private String group;

    @ApiModelProperty(value = "任务描述", required = true)
    @NotBlank(message = "任务描述不能为空")
    @Length(max = 255, message = "任务描述长度不能超过255字符")
    private String description;

    @ApiModelProperty(value = "job-class", required = true)
    @NotBlank(message = "job-class不能为空")
    @Length(max = 255, message = "job-class长度不能超过255字符")
    private String jobClassName;

    @ApiModelProperty(value = "cron表达式", required = true)
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    @ApiModelProperty(value = "补偿模式（-1：补偿所有，1：只补偿一次，2：不进行补偿）", required = true, example = "2")
    @NotNull(message = "补偿模式不能为空")
    private Integer misfireInstruction;

    @ApiModelProperty(value = "任务权重", required = true, example = "5")
    @NotNull(message = "任务权重不能为空")
    private Integer priority;

    @ApiModelProperty(value = "上次执行时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String previousFireTime;

    @ApiModelProperty(value = "下次执行时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String nextFireTime;

    @ApiModelProperty(value = "任务开始时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String startTime;

    @ApiModelProperty(value = "任务状态（NORMAL：正常， PAUSED：暂停）", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String state;

    @Override
    public int compareTo(QuartzJobEntity o) {
        return this.name.compareTo(o.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(String previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
