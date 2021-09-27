package fun.bookish.blueberry.quartz.service.impl;

import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.SpringUtils;
import fun.bookish.blueberry.quartz.entity.MisfireInstruction;
import fun.bookish.blueberry.quartz.entity.QuartzJobEntity;
import fun.bookish.blueberry.quartz.service.IQuartzService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LIUXINDONG
 */
@Service
public class QuartzServiceImpl implements IQuartzService {

    @Autowired
    private Scheduler scheduler;

    @Override
    public List<String> findJobClassNames() {
        return SpringUtils
                .getBeansByType(Job.class)
                .stream()
                .map(job -> job.getClass().getTypeName())
                .collect(Collectors.toList());
    }

    @Override
    public void addJob(QuartzJobEntity quartzTask) {
        String jobClassName = quartzTask.getJobClassName();
        try {
            Class<?> jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder
                    .newJob(((Job) (jobClass.newInstance())).getClass())
                    .withIdentity(quartzTask.getName(), quartzTask.getGroup())
                    .withDescription(quartzTask.getDescription())
                    .build();

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                    .cronSchedule(quartzTask.getCronExpression());

            switch (quartzTask.getMisfireInstruction()) {
                case 2:
                    cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
                    break;
                case 1:
                    cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                    break;
                case -1:
                    cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                    break;
                default:
                    throw new ManualRollbackException("创建任务失败: 补偿策略非法");
            }

            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(quartzTask.getName(), quartzTask.getGroup())
                    .withSchedule(cronScheduleBuilder)
                    .withPriority(quartzTask.getPriority())
                    .withDescription(quartzTask.getDescription())
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            throw new ManualRollbackException("创建任务失败: " + e.getMessage());
        }
    }

    @Override
    public void updateJob(QuartzJobEntity quartzTask) {
        try {
            deleteJob(quartzTask.getName(), quartzTask.getGroup());
            addJob(quartzTask);
        } catch (Exception e) {
            throw new ManualRollbackException("更新任务失败: " + e.getMessage());
        }
    }

    @Override
    public void pauseJob(String name, String group) {
        try {
            scheduler.pauseJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new ManualRollbackException("暂停任务失败: " + e.getMessage());
        }
    }

    @Override
    public void resumeJob(String name, String group) {
        try {
            scheduler.resumeJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new ManualRollbackException("恢复任务失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteJob(String name, String group) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));
            scheduler.deleteJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new ManualRollbackException("恢复任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<QuartzJobEntity> allJobs(String name, String group, String description, String state) {
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
            List<QuartzJobEntity> tasks = jobKeys
                    .stream()
                    .map(jobKey -> {
                        try {
                            QuartzJobEntity quartzTask = new QuartzJobEntity();
                            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            quartzTask.setName(jobDetail.getKey().getName());
                            quartzTask.setGroup(jobDetail.getKey().getGroup());
                            quartzTask.setDescription(jobDetail.getDescription());
                            quartzTask.setJobClassName(jobDetail.getJobClass().getTypeName());
                            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
                            quartzTask.setState(scheduler.getTriggerState(trigger.getKey()).name());
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            quartzTask.setCronExpression((cronTrigger).getCronExpression());
                            quartzTask.setMisfireInstruction(cronTrigger.getMisfireInstruction());
                            quartzTask.setPriority(trigger.getPriority());
                            Date previousFireTime = trigger.getPreviousFireTime();
                            quartzTask.setPreviousFireTime(previousFireTime == null ? "" : DateUtils.formatDate(previousFireTime, "yyy-MM-dd HH:mm:ss"));
                            Date nextFireTime = trigger.getNextFireTime();
                            quartzTask.setNextFireTime(nextFireTime == null ? "" : DateUtils.formatDate(nextFireTime, "yyy-MM-dd HH:mm:ss"));
                            Date startTime = trigger.getStartTime();
                            quartzTask.setStartTime(startTime == null ? "" : DateUtils.formatDate(startTime, "yyy-MM-dd HH:mm:ss"));
                            return quartzTask;
                        } catch (SchedulerException e) {
                            throw new ManualRollbackException("查询任务列表失败: " + e.getMessage());
                        }
                    })
                    .sorted().collect(Collectors.toList());
            if (StringUtils.isNotBlank(name)) {
                tasks = tasks.stream().filter(t -> t.getName().contains(name)).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(group)) {
                tasks = tasks.stream().filter(t -> t.getGroup().contains(group)).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(description)) {
                tasks = tasks.stream().filter(t -> t.getDescription().contains(description)).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(state)) {
                tasks = tasks.stream().filter(t -> t.getState().equals(state)).collect(Collectors.toList());
            }
//            Page<QuartzTask> res = new Page<>();
//            // 处理分页
//            if (page <= 0) {
//                res.addAll(tasks);
//                res.setTotal(tasks.size());
//            } else if (pageSize <= 0) {
//                res.setTotal(0);
//            } else {
//                int skip = (page - 1) * pageSize;
//                res.setTotal(tasks.size());
//                res.addAll(tasks.stream().skip(skip).limit(pageSize).collect(Collectors.toList()));
//            }
            return tasks;
        } catch (Exception e) {
            throw new ManualRollbackException("查询任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public void resumeAllJobs() {
        try {
            scheduler.resumeAll();
        } catch (Exception e) {
            throw new ManualRollbackException("启动所有任务失败：" + e.getMessage());
        }
    }

    @Override
    public void pauseAllJobs() {
        try {
            scheduler.pauseAll();
        } catch (Exception e) {
            throw new ManualRollbackException("暂停所有任务失败：" + e.getMessage());
        }
    }

    @Override
    public void startScheduler() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new ManualRollbackException("启动任务调度器失败：" + e.getMessage());
        }
    }

    @Override
    public void standbyScheduler() {
        try {
            scheduler.standby();
        } catch (Exception e) {
            throw new ManualRollbackException("暂停任务调度器失败：" + e.getMessage());
        }
    }

    @Override
    public void shutdownScheduler() {
        try {
            scheduler.shutdown();
        } catch (Exception e) {
            throw new ManualRollbackException("关闭任务调度器失败：" + e.getMessage());
        }
    }

    @Override
    public String schedulerState() {
        try {
            if (scheduler.isShutdown()) {
                return "SHUTDOWN";
            } else if (scheduler.isInStandbyMode()) {
                return "STANDBY";
            } else {
                return "START";
            }
        } catch (Exception e) {
            throw new ManualRollbackException("查询任务调度器状态失败：" + e.getMessage());
        }
    }

    @Override
    public List<MisfireInstruction> findMisfireInstructions() {
        List<MisfireInstruction> result = new ArrayList<>();
        result.add(new MisfireInstruction("不进行补偿", 2));
        result.add(new MisfireInstruction("只补偿一次", 1));
        result.add(new MisfireInstruction("补偿所有", -1));
        return result;
    }

}
