package fun.bookish.blueberry.quartz.listener;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务调度器监听器
 * @author Don9
 */
@Component
public class QuartzSchedulerListener implements SchedulerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzSchedulerListener.class);

    @Override
    public void jobScheduled(Trigger trigger) {

    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {

    }

    @Override
    public void triggerFinalized(Trigger trigger) {

    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {

    }

    @Override
    public void triggersPaused(String triggerGroup) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String triggerGroup) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {

    }

    @Override
    public void jobDeleted(JobKey jobKey) {

    }

    @Override
    public void jobPaused(JobKey jobKey) {

    }

    @Override
    public void jobsPaused(String jobGroup) {

    }

    @Override
    public void jobResumed(JobKey jobKey) {

    }

    @Override
    public void jobsResumed(String jobGroup) {

    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {

    }

    @Override
    public void schedulerInStandbyMode() {
        LOGGER.info("调度器暂停");
    }

    @Override
    public void schedulerStarted() {
        LOGGER.info("调度器启动");
    }

    @Override
    public void schedulerStarting() {

    }

    @Override
    public void schedulerShutdown() {
        LOGGER.info("调度器关闭");
    }

    @Override
    public void schedulerShuttingdown() {
    }

    @Override
    public void schedulingDataCleared() {

    }
}
