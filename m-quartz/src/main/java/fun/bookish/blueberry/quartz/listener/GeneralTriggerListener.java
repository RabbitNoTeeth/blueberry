package fun.bookish.blueberry.quartz.listener;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;


/**
 * 通用的job监听器
 * @author Don9
 */
@Component
public class GeneralTriggerListener implements TriggerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralTriggerListener.class);

    @Override
    public String getName() {
        return "GeneralTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        String name = trigger.getJobKey().getName();
        String group = trigger.getJobKey().getGroup();
        String description = context.getJobDetail().getDescription();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("任务开始执行 [name = {}, group = {}, description = {}]", name, group, description);
        }
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        String name = trigger.getJobKey().getName();
        String group = trigger.getJobKey().getGroup();
        String description = trigger.getDescription();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("任务补偿 [name = {}, group = {}, description = {}]", name, group, description);
        }
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        String name = trigger.getJobKey().getName();
        String group = trigger.getJobKey().getGroup();
        String description = context.getJobDetail().getDescription();
        String nextFireTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(trigger.getNextFireTime());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("任务执行结束 [name = {}, group = {}, description = {}, nextFireTime = {}]", name, group, description, nextFireTime);
        }
    }

}
