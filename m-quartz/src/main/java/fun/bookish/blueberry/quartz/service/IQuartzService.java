package fun.bookish.blueberry.quartz.service;



import fun.bookish.blueberry.quartz.entity.MisfireInstruction;
import fun.bookish.blueberry.quartz.entity.QuartzJobEntity;

import java.util.List;

/**
 * @author LIUXINDONG
 */
public interface IQuartzService {

    /**
     * 查询job类名
     *
     * @return
     */
    List<String> findJobClassNames();

    /**
     * 新建任务
     *
     * @param entity
     * @return
     */
    void addJob(QuartzJobEntity entity);

    /**
     * 更新任务
     *
     * @param entity
     */
    void updateJob(QuartzJobEntity entity);

    /**
     * 暂停任务
     *
     * @param name
     * @param group
     */
    void pauseJob(String name, String group);

    /**
     * 恢复任务
     *
     * @param name
     * @param group
     */
    void resumeJob(String name, String group);

    /**
     * 删除任务
     *
     * @param name
     * @param group
     */
    void deleteJob(String name, String group);

    /**
     * 查询所有任务
     *
     * @return
     * @param name
     * @param group
     * @param state
     */
    List<QuartzJobEntity> allJobs(String name, String group, String description, String state);

    /**
     * 启动所有任务
     */
    void resumeAllJobs();

    /**
     * 暂停所有任务
     */
    void pauseAllJobs();

    /**
     * 启动任务调度器
     */
    void startScheduler();

    /**
     * 暂停任务调度器
     */
    void standbyScheduler();

    /**
     * 关闭任务调度器
     */
    void shutdownScheduler();

    /**
     * 查询任务调度器状态
     */
    String schedulerState();

    /**
     * 查询补偿策略列表
     * @return
     */
    List<MisfireInstruction> findMisfireInstructions();
}
