package fun.bookish.blueberry.server.schedule;

import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.server.schedule.lock.ScheduleLockManager;
import fun.bookish.blueberry.server.videostream.entity.VideoStreamPO;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import fun.bookish.blueberry.server.schedule.conf.VideoNoReaderCheckProperties;
import fun.bookish.blueberry.server.zlmedia.connect.ZLMediaKitConnection;
import fun.bookish.blueberry.server.zlmedia.model.ZLMediaListResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 无人观看检测任务
 */
@Component
public class VideoNoReaderCheckTask implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoNoReaderCheckTask.class);

    @Autowired
    private VideoNoReaderCheckProperties videoNoReaderCheckProperties;
    @Autowired
    private ZLMediaKitConnection zlMediaKitConnection;
    @Autowired
    private IVideoStreamService videoStreamService;
    @Autowired
    private ScheduleLockManager scheduleLockManager;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("无人观看检测任务开始执行...");
        long start = System.currentTimeMillis();
        int streamAliveSecondsWithoutReader = videoNoReaderCheckProperties.getAliveSeconds();
        Lock lock = scheduleLockManager.getStreamLock();
        try {
            lock.lock();
            List<String> streamRemoved = new ArrayList<>();
            List<String> streamRecovered = new ArrayList<>();
            List<String> streamClosed = new ArrayList<>();
            // 查询数据库中所有视频流信息
            List<VideoStreamPO> mediaStreamsInDb = videoStreamService.list();
            // 查询媒体服务器中所有视频流信息
            ZLMediaListResponse zlMediaListResponse = zlMediaKitConnection.queryStreamList();
            if (zlMediaListResponse == null || zlMediaListResponse.getData() == null) {
                // 找出并删除数据库中已失效的视频流信息(即媒体服务器中已不存在该视频流)
                for (VideoStreamPO stream : mediaStreamsInDb) {
                    String streamId = stream.getId();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("视频流[{}]在媒体服务器中已不存在，删除数据库中此记录", streamId);
                    }
                    videoStreamService.removeById(streamId);
                    streamRemoved.add(streamId);
                }
            } else {
                List<ZLMediaListResponse.ZLMedia> mediaStreamsInServer = zlMediaListResponse.getData();
                // 查找所有数据库中未保存的视频流信息
                List<String> mediaStreamIdsInDb = mediaStreamsInDb.stream().map(VideoStreamPO::getId).collect(Collectors.toList());
                mediaStreamsInServer.stream().filter(s -> !mediaStreamIdsInDb.contains(s.getStream())).forEach(stream -> {
                    // 判断是否无人观看
                    String streamId = stream.getStream();
                    Integer totalReaderCount = stream.getTotalReaderCount();
                    if (totalReaderCount == 0) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("视频流[{}]已无人观看，关闭该视频流", streamId);
                        }
                        // 无人观看，关闭视频流
                        zlMediaKitConnection.closeStream(streamId);
                        streamClosed.add(streamId);
                    }
                });
                // 遍历数据库中视频流信息
                for (VideoStreamPO stream : mediaStreamsInDb) {
                    String streamId = stream.getId();
                    Integer status = stream.getStatus();
                    if (status == 1) {
                        // 状态为已连接的视频流，先确认媒体服务器中是否存在该视频流
                        Optional<ZLMediaListResponse.ZLMedia> streamExistsOptional = mediaStreamsInServer.stream().filter(ms -> ms.getStream().equals(streamId)).findFirst();
                        if (!streamExistsOptional.isPresent()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("视频流[{}]在媒体服务器中已不存在，删除数据库中此记录", streamId);
                            }
                            // 媒体服务器中不存在该视频流，那么删除数据库中此视频信息
                            videoStreamService.removeById(streamId);
                            streamRemoved.add(streamId);
                        } else {
                            // 媒体服务器中存在该视频流，判断是否无人观看
                            ZLMediaListResponse.ZLMedia zlMedia = streamExistsOptional.get();
                            Integer totalReaderCount = zlMedia.getTotalReaderCount();
                            Long aliveSecond = zlMedia.getAliveSecond();
                            if (totalReaderCount == 0 && aliveSecond > streamAliveSecondsWithoutReader) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("视频流[{}]已无人观看，关闭该视频流", streamId);
                                }
                                // 无人观看，关闭视频流
                                videoStreamService.closeStream(stream);
                                streamClosed.add(streamId);
                                streamRemoved.add(streamId);
                            }
                        }
                    } else if (status == 2) {
                        // 状态为已关闭的视频流，先确认媒体服务器中是否存在该视频流
                        Optional<ZLMediaListResponse.ZLMedia> streamExistsOptional = mediaStreamsInServer.stream().filter(ms -> ms.getStream().equals(streamId)).findFirst();
                        if (!streamExistsOptional.isPresent()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("视频流[{}]在媒体服务器中已不存在，删除数据库中此记录", streamId);
                            }
                            // 媒体服务器中不存在该视频流，那么删除数据库中此视频信息
                            videoStreamService.removeById(streamId);
                            streamRemoved.add(streamId);
                        } else {
                            // 媒体服务器中存在该视频流，判断是否无人观看
                            ZLMediaListResponse.ZLMedia zlMedia = streamExistsOptional.get();
                            Integer totalReaderCount = zlMedia.getTotalReaderCount();
                            Long aliveSecond = zlMedia.getAliveSecond();
                            if (totalReaderCount == 0 && aliveSecond > streamAliveSecondsWithoutReader) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("视频流[{}]已无人观看，关闭该视频流", streamId);
                                }
                                // 无人观看，关闭视频流
                                videoStreamService.closeStream(stream);
                                streamClosed.add(streamId);
                                streamRemoved.add(streamId);
                            } else {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("视频流[{}]虽为已关闭状态，但仍有人观看，恢复为已连接状态", streamId);
                                }
                                // 有人观看，更新视频流为已连接状态
                                VideoStreamPO updateMediaStream = new VideoStreamPO();
                                updateMediaStream.setId(streamId);
                                updateMediaStream.setStatus(1);
                                videoStreamService.updateById(updateMediaStream);
                                streamRecovered.add(streamId);
                            }
                        }
                    }
                }
            }
            LOGGER.info("无人观看检测任务执行完成({}ms). streamInDbRemoved:{}, streamInDbRecovered:{}, streamClosed:{}", System.currentTimeMillis() - start,
                    JsonUtils.encode(streamRemoved), JsonUtils.encode(streamRecovered), JsonUtils.encode(streamClosed));
        } catch (Exception e) {
            LOGGER.error("无人观看检测任务异常", e);
        } finally {
            lock.unlock();
        }
    }

}
