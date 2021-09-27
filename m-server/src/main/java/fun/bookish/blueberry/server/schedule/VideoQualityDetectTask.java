package fun.bookish.blueberry.server.schedule;

import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.schedule.lock.ScheduleLockManager;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticPO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import fun.bookish.blueberry.server.videoqualitydetect.detect.entity.VideoStreamQualityDetectResult;
import fun.bookish.blueberry.server.videoqualitydetect.detect.VideoStreamDetectService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;


/**
 * 视频质量检测任务
 */
public class VideoQualityDetectTask implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoQualityDetectTask.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private VideoStreamDetectService videoStreamDetectService;
    @Autowired
    private IVideoQualityDetectArithmeticService videoQualityDetectArithmeticService;
    @Autowired
    private ScheduleLockManager scheduleLockManager;

    private final ExecutorService streamDetectExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("视频质量检测任务开始执行...");
        long start = System.currentTimeMillis();
        String fireTime = DateUtils.formatDate(jobExecutionContext.getScheduledFireTime(), "yyyyMMddHHmmss");
        Lock lock = scheduleLockManager.getStreamLock();
        try {
            lock.lock();
            int successCount = 0;
            int errorCount = 0;
            int totalCount = 0;
            int qualityErrorCount = 0;
            // 查询所有启用的视频质量检测算法
            List<VideoQualityDetectArithmeticPO> arithmeticPOS = videoQualityDetectArithmeticService
                    .query()
                    .eq("enable", 1)
                    .orderByAsc("priority")
                    .list();
            if (arithmeticPOS.size() == 0) {
                LOGGER.info("视频质量检测任务执行完成({}ms). success:{}, failed:{}, qualityError:{}, total:{}", System.currentTimeMillis() - start, successCount, errorCount, qualityErrorCount, totalCount);
                return;
            }
            // 查询所有在线设备
            List<DevicePO> devices = deviceService.query().eq("online", 1).list();
            if (devices.size() > 0) {
                // 创建任务列表
                List<Callable<VideoStreamQualityDetectResult>> tasks = new ArrayList<>();
                devices.forEach(device -> {
                    List<ChannelPO> channels = channelService.query().eq("device_id", device.getId()).list();
                    channels.forEach(channel -> tasks.add(createTask(device, channel, fireTime, arithmeticPOS)));
                });
                totalCount += tasks.size();
                // 多线程执行任务列表
                List<Future<VideoStreamQualityDetectResult>> invokeResults = streamDetectExecutor.invokeAll(tasks);
                for (Future<VideoStreamQualityDetectResult> result : invokeResults) {
                    try {
                        VideoStreamQualityDetectResult detectResult = result.get();
                        if (detectResult.getHasError()) {
                            errorCount++;
                        } else {
                            successCount++;
                            if (detectResult.getHasQualityError()) {
                                qualityErrorCount++;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("获取质量检测结果失败", e);
                    }
                }
            }
            LOGGER.info("视频质量检测任务执行完成({}ms). success:{}, failed:{}, qualityError:{}, total:{}", System.currentTimeMillis() - start, successCount, errorCount, qualityErrorCount, totalCount);
        } catch (Exception e) {
            LOGGER.error("视频质量检测任务异常", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 创建FutureTask
     *
     * @param device
     * @param channel
     * @param fireTime
     * @param arithmeticPOS
     * @return
     */
    private Callable<VideoStreamQualityDetectResult> createTask(DevicePO device, ChannelPO channel, String fireTime, List<VideoQualityDetectArithmeticPO> arithmeticPOS) {
        return () -> videoStreamDetectService.detectStreamQuality(device, channel, fireTime, arithmeticPOS);
    }

}
