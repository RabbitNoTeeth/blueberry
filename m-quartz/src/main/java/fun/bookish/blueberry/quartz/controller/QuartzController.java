package fun.bookish.blueberry.quartz.controller;

import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.quartz.entity.MisfireInstruction;
import fun.bookish.blueberry.quartz.entity.QuartzJobEntity;
import fun.bookish.blueberry.quartz.service.IQuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 设备表 前端控制器
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Api(tags = "定时任务管理")
@RestController
@EnableResponseBodyJsonWrap
@RequestMapping("/api/v1/quartz")
public class QuartzController {

    @Autowired
    private IQuartzService quartzService;

    @ApiOperation("启动任务调度器")
    @PostMapping("/start")
    public String startScheduler() {
        quartzService.startScheduler();
        return "success";
    }

    @ApiOperation("暂停任务调度器")
    @PostMapping("/standby")
    public String standbyScheduler() {
        quartzService.standbyScheduler();
        return "success";
    }

    @ApiOperation("关闭任务调度器")
    @PostMapping("/shutdown")
    public String shutdownScheduler() {
        quartzService.shutdownScheduler();
        return "success";
    }

    @ApiOperation("查询任务调度器状态")
    @GetMapping("/state")
    public String schedulerState() {
        return quartzService.schedulerState();
    }

    @ApiOperation("加载job类名列表")
    @GetMapping("/job/classNames")
    public List<String> jobClassNames() {
        return quartzService.findJobClassNames();
    }

    @ApiOperation("查询补偿策略列表")
    @GetMapping("/job/misfireInstruction/list")
    public List<MisfireInstruction> misfireInstructionList() {
        return quartzService.findMisfireInstructions();
    }

    @ApiOperation("添加定时任务")
    @PostMapping("/job/add")
    public String addTask(@Valid QuartzJobEntity quartzTask) {
        quartzService.addJob(quartzTask);
        return quartzTask.getName();
    }

    @ApiOperation("更新定时任务")
    @PostMapping("/job/update")
    public String updateTask(@Valid QuartzJobEntity quartzTask) {
        quartzService.updateJob(quartzTask);
        return quartzTask.getName();
    }

    @ApiOperation("暂停定时任务")
    @PostMapping("/job/pause")
    public String pauseTask(String name, String group) {
        quartzService.pauseJob(name, group);
        return name;
    }

    @ApiOperation("启动定时任务")
    @PostMapping("/job/resume")
    public String resumeTask(String name, String group) {
        quartzService.resumeJob(name, group);
        return name;
    }

    @ApiOperation("删除定时任务")
    @PostMapping("/job/delete")
    public String deleteTask(String name, String group) {
        quartzService.deleteJob(name, group);
        return name;
    }

    @ApiOperation("查询定时任务列表")
    @GetMapping("/job/list")
    public List<QuartzJobEntity> allTasks(String name,
                                          String group,
                                          String description,
                                          String state) {
        return quartzService.allJobs(name, group, description, state);
    }

    @ApiOperation("启动所有任务")
    @PostMapping("/job/resumeAll")
    public void resumeAllTasks() {
        quartzService.resumeAllJobs();
    }

    @ApiOperation("暂停所有任务")
    @PostMapping("/job/pauseAll")
    public void pauseAllTasks() {
        quartzService.pauseAllJobs();
    }

}
