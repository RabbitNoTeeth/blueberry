package fun.bookish.blueberry.quartz.listener;


import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * quartz自动配置监听器
 *      程序启动完成后，自动加载数据库中的定时任务并启动
 * @author Don9
 */
@Component
public class QuartzAutoStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private GeneralTriggerListener generalTriggerListener;

    @Autowired
    private QuartzSchedulerListener quartzSchedulerListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if(scheduler.isStarted()){
                scheduler.getListenerManager().addSchedulerListener(quartzSchedulerListener);
                scheduler.getListenerManager().addTriggerListener(generalTriggerListener, EverythingMatcher.allTriggers());
                scheduler.start();
            }
        } catch (SchedulerException e) {
            throw new IllegalStateException("定时任务调度器启动失败", e);
        }
    }
}
