package fun.bookish.blueberry.server.schedule;

import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.opencv.detector.DifferenceDetector;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.schedule.lock.ScheduleLockManager;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticPO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import fun.bookish.blueberry.server.videoqualitydetect.arithmeticapplydevice.service.IVideoQualityDetectArithmeticApplyDeviceService;
import fun.bookish.blueberry.server.videoqualitydetect.detect.VideoStreamDetectService;
import fun.bookish.blueberry.server.videoqualitydetect.detect.entity.VideoStreamQualityDetectResult;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.service.IVideoQualityDetectRecordService;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamSnapshot;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;


/**
 * 视频差异检测任务
 */
public class VideoDifferenceDetectTestTask implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoDifferenceDetectTestTask.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private IVideoStreamService videoStreamService;
    @Autowired
    private IVideoQualityDetectRecordService videoQualityDetectRecordService;
    @Autowired
    private ScheduleLockManager scheduleLockManager;

    private final ExecutorService streamDetectExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("视频差异检测任务开始执行...");
        long start = System.currentTimeMillis();
        String fireTime = DateUtils.formatDate(jobExecutionContext.getScheduledFireTime(), "yyyyMMddHHmmss");
        Lock lock = scheduleLockManager.getStreamLock();
        try {
            lock.lock();
            String deviceId = "13020000001320000201";
            String channelId = "13020000001320000201";
            DevicePO device = deviceService.queryById(deviceId);
            if (device == null) {
                LOGGER.warn("视频差异检测任务执行中断, 未找到设备：{}", deviceId);
                return;
            }
            ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
            if (channel == null) {
                LOGGER.warn("视频差异检测任务执行中断, 未找到通道：{}", channelId);
                return;
            }
            // 进行视频截图
            VideoStreamSnapshot snapshot = videoStreamService.takeSnapshot(device, channel, fireTime);
            // 创建任务列表
            List<Callable<DifferenceDetector.Result>> tasks = new ArrayList<>();
            DifferenceDetector differenceDetector = new DifferenceDetector();
            // 与最近的指定时间段内的截图对比
            String startTime = DateUtils.formatDate(DateUtils.parseDateStr(fireTime, "yyyyMMddHHmmss").minusMinutes(30), "yyyyMMddHHmmss");
            String dirPath = snapshot.getDirPath();
            File dir = new File(dirPath);
            File[] images = dir.listFiles();
            for (File image : images) {
                String imageName = image.getName();
                String timeInImageName = imageName.replace(".jpeg", "");
                if (timeInImageName.compareTo(startTime) >= 0 && !imageName.equals(snapshot.getFileName())) {
                    tasks.add(createTask(differenceDetector, snapshot.getSavePath(), dirPath + "/" + imageName, fireTime, deviceId, channelId));
                }
            }
            // 多线程执行任务列表
            List<Future<DifferenceDetector.Result>> invokeResults = streamDetectExecutor.invokeAll(tasks);
            for (Future<DifferenceDetector.Result> result : invokeResults) {
                try {
                    result.get();
                } catch (Exception e) {
                    LOGGER.error("获取质量检测结果失败", e);
                }
            }
            LOGGER.info("视频差异检测任务执行完成({}ms)", System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("视频差异检测任务异常", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 创建FutureTask
     *
     * @param imagePath1
     * @param imagePath2
     * @param fireTime
     * @param deviceId
     * @param channelId
     * @return
     */
    private Callable<DifferenceDetector.Result> createTask(DifferenceDetector differenceDetector, String imagePath1, String imagePath2, String fireTime, String deviceId, String channelId) {
        return () -> {
            try {
                Mat image1 = opencv_imgcodecs.imread(imagePath1);
                Mat image2 = opencv_imgcodecs.imread(imagePath2);
                DifferenceDetector.Result result = differenceDetector.detect(image1, image2);
                // 保存检测结果到数据库
                // 保存结果至数据库
                VideoQualityDetectRecordPO record = new VideoQualityDetectRecordPO();
                record.setArithmeticCode("ANGLE_CHANGE");
                record.setArithmeticName("角度变化检测");
                record.setDeviceId(deviceId);
                record.setChannelId(channelId);
                record.setImagePath(imagePath1 + "," + imagePath2);
                record.setDetail(JsonUtils.encode(result));
                record.setHasError(0);
                if (result.isHasError()) {
                    record.setHasQualityError(1);
                    record.setError("ANGLE_CHANGE");
                } else {
                    record.setHasQualityError(0);
                }
                record.setCreatedAt(fireTime);
                videoQualityDetectRecordService.save(record);
                return result;
            } catch (Exception e) {
                LOGGER.error("视频差异检测异常, image1: {}, image2: {}", imagePath1, imagePath2, e);
                // 保存结果至数据库
                VideoQualityDetectRecordPO record = new VideoQualityDetectRecordPO();
                record.setArithmeticCode("ANGLE_CHANGE");
                record.setArithmeticName("角度变化检测");
                record.setDeviceId(deviceId);
                record.setChannelId(channelId);
                record.setImagePath(imagePath1 + "," + imagePath2);
                record.setHasError(1);
                record.setError(e.getMessage());
                record.setCreatedAt(fireTime);
                videoQualityDetectRecordService.save(record);
                return null;
            }
        };
    }

}
