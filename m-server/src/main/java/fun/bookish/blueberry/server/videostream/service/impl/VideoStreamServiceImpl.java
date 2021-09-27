package fun.bookish.blueberry.server.videostream.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.utils.Base64Utils;
import fun.bookish.blueberry.core.utils.FileUtils;
import fun.bookish.blueberry.core.utils.HexUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.server.device.entity.DeviceType;
import fun.bookish.blueberry.server.schedule.conf.VideoQualityDetectProperties;
import fun.bookish.blueberry.server.videostream.entity.*;
import fun.bookish.blueberry.server.videostream.mapper.VideoStreamMapper;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.zlmedia.conf.MediaProperties;
import fun.bookish.blueberry.server.zlmedia.connect.ZLMediaKitConnection;
import fun.bookish.blueberry.server.zlmedia.model.ZLMediaListResponse;
import fun.bookish.blueberry.server.zlmedia.model.ZLStreamAddStreamProxyResponse;
import fun.bookish.blueberry.server.zlmedia.model.ZLStreamCloseResponse;
import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.media.SipMediaCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaPlayParam;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaStopParam;
import fun.bookish.blueberry.sip.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <p>
 * 视频流表 服务实现类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
@Service
public class VideoStreamServiceImpl extends ServiceImpl<VideoStreamMapper, VideoStreamPO> implements IVideoStreamService {

    private final IDeviceService deviceService;
    private final IChannelService channelService;
    private final VideoStreamManager videoStreamManager;
    private final ZLMediaKitConnection zlMediaKitConnection;
    private final MediaProperties mediaProperties;

    @Autowired
    public VideoStreamServiceImpl(IDeviceService deviceService,
                                  IChannelService channelService,
                                  SipServer sipServer,
                                  VideoStreamMapper videoStreamMapper,
                                  ZLMediaKitConnection zlMediaKitConnection,
                                  MediaProperties mediaProperties) {
        this.deviceService = deviceService;
        this.channelService = channelService;
        this.videoStreamManager = new VideoStreamManager(videoStreamMapper, mediaProperties, sipServer, zlMediaKitConnection);
        this.zlMediaKitConnection = zlMediaKitConnection;
        this.mediaProperties = mediaProperties;
    }

    /**
     * 查询视频流列表
     *
     * @return
     */
    public List<VideoStreamVO> queryList() {
        List<VideoStreamVO> res = new ArrayList<>();
        ZLMediaListResponse mediaListResponse = zlMediaKitConnection.queryStreamList();
        if (mediaListResponse != null) {
            List<ZLMediaListResponse.ZLMedia> streams = mediaListResponse.getData();
            if (streams != null) {
                streams.forEach(stream -> {
                    String streamId = stream.getStream();
                    VideoStreamVO videoStreamVO = new VideoStreamVO();
                    videoStreamVO.setId(streamId);
                    String flvUrl = "ws://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/" + stream.getApp() + "/" + streamId + ".flv";
                    videoStreamVO.setFlvUrl(flvUrl);
                    String snapQueryParam = "http://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/" + stream.getApp() + "/" + streamId + ".flv";
                    videoStreamVO.setSnap(Base64Utils.encode(snapQueryParam));
                    res.add(videoStreamVO);
                });
            }
        }
        return res;
    }

    /**
     * 实时视频点播
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public VideoStreamPlayResponse play(String deviceId, String channelId) throws Exception {
        // 验证设备是否存在
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("设备" + deviceId + "不存在");
        }
        // 验证channel是否存在
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("通道" + channelId + "不存在");
        }
        // 将播放请求放入管理器队列中
        PlayResponse playResponse = videoStreamManager.playStream(new PlayRequest(device, channel));
        if (playResponse.isSuccess()) {
            VideoStreamPlayResponse res = new VideoStreamPlayResponse();
            res.setDeviceId(deviceId);
            res.setChannelId(channelId);
            res.setFlv(playResponse.getFlv());
            return res;
        } else {
            throw new ManualRollbackException("播放失败");
        }
    }

    /**
     * 停止实时媒体流
     *
     * @param streamId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public VideoStreamStopResponse stop(String streamId) throws Exception {
        videoStreamManager.stopStream(streamId);
        VideoStreamStopResponse res = new VideoStreamStopResponse();
        res.setStreamId(streamId);
        return res;
    }

    @Override
    public List<VideoStreamPO> queryListByDeviceIdAndChannelId(String deviceId, String channelId) {
        return query().eq("device_id", deviceId).eq("channel_id", channelId).list();
    }

    @Override
    public VideoStreamPO queryById(String id) {
        return query().eq("id", id).one();
    }

    @Override
    public String updateBySSRC(VideoStreamPO entity) {
        String ssrc = entity.getSsrc();
        UpdateWrapper<VideoStreamPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ssrc", ssrc);
        update(entity, updateWrapper);
        return ssrc;
    }

    @Override
    public PlayResponse playStream(PlayRequest playRequest) {
        return videoStreamManager.playStream(playRequest);
    }

    @Override
    public void closeStream(String streamId) throws Exception {
        videoStreamManager.closeStream(streamId);
    }

    @Override
    public void closeStream(VideoStreamPO stream) throws Exception {
        videoStreamManager.closeStream(stream);
    }

    @Override
    public VideoStreamSnapshot takeSnapshot(String deviceId, String channelId) throws Exception {
        return takeSnapshot(deviceId, channelId, null);
    }

    @Override
    public VideoStreamSnapshot takeSnapshot(String deviceId, String channelId, String time) throws Exception {
        // 验证设备是否存在
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("设备" + deviceId + "不存在");
        }
        // 验证channel是否存在
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("通道" + channelId + "不存在");
        }
        return takeSnapshot(device, channel, time);
    }

    @Override
    public VideoStreamSnapshot takeSnapshot(DevicePO device, ChannelPO channel) throws Exception {
        return takeSnapshot(device, channel, null);
    }

    @Override
    public VideoStreamSnapshot takeSnapshot(DevicePO device, ChannelPO channel, String time) throws Exception {
        VideoStreamSnapshot res = new VideoStreamSnapshot();
        String deviceId = device.getId();
        String channelId = channel.getId();
        // 进行视频点播
        IVideoStreamService.PlayResponse playResponse = playStream(new IVideoStreamService.PlayRequest(device, channel));
        if (!playResponse.isSuccess()) {
            throw new IllegalStateException("视频点播失败");
        }
        // 点播成功后，获取视频截图
        String flv = playResponse.getFlv();
        byte[] snapBytes = zlMediaKitConnection.querySnap(flv.replace("ws://", "http://"));
        // todo 关闭视频流(目前不进行关闭动作，由无人观看检测任务来完成，如果后期服务器压力过大，可在此处通过单独的线程进行关闭，以免阻塞)
        // 保存图片至本地
        String saveDir = "/" + deviceId + "/" + channelId;
        String savePath = mediaProperties.getSnapshotSavaPath() + saveDir;
        String snapName = (StringUtils.isBlank(time) ? DateUtils.getNowDateTimeStr() : time) + ".jpeg";
        FileUtils.save(savePath, snapName, snapBytes);
        // 填充返回结果
        res.setData(snapBytes);
        res.setDirPath(savePath);
        res.setSavePath(savePath + "/" + snapName);
        res.setFileName(snapName);
        return res;
    }

    private static class VideoStreamManager {

        private static final Logger LOGGER = LoggerFactory.getLogger(VideoStreamManager.class);

        private final VideoStreamMapper videoStreamMapper;
        private final MediaProperties mediaProperties;
        private final SipServer sipServer;
        private final ZLMediaKitConnection zlMediaKitConnection;

        /**
         * 播放请求set，用于请求去重
         */
        private final Set<String> playRequestSet = new ConcurrentSkipListSet<>();

        private VideoStreamManager(VideoStreamMapper videoStreamMapper, MediaProperties mediaProperties, SipServer sipServer, ZLMediaKitConnection zlMediaKitConnection) {
            this.videoStreamMapper = videoStreamMapper;
            this.mediaProperties = mediaProperties;
            this.sipServer = sipServer;
            this.zlMediaKitConnection = zlMediaKitConnection;
        }

        /**
         * 播放视频流
         *
         * @param playRequest
         * @throws Exception
         */
        private PlayResponse playStream(PlayRequest playRequest) {
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            String channelId = playRequest.getChannel().getId();
            String playRequestKey = playRequest.getKey();
            try {
                PlayResponse res = null;
                if (playRequestSet.contains(playRequestKey)) {
                    VideoStreamPO availableStream = null;
                    // playRequestSet中已存在playRequestKey，说明有正在处理的播放请求，直接查询数据库
                    int retry = 0;
                    while (retry < 5) {
                        // 查询数据库中当前设备通道的视频流信息，并与媒体服务器中视频流进行对比，查找有效的视频信息
                        QueryWrapper<VideoStreamPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("device_id", deviceId).eq("channel_id", channelId);
                        List<VideoStreamPO> streamsInDb = videoStreamMapper.selectList(queryWrapper);
                        availableStream = confirmMediaStreams(streamsInDb);
                        if (availableStream != null) {
                            // 存在有效的视频流，直接返回
                            res = new PlayResponse(true, availableStream.getId(), availableStream.getFlv());
                            break;
                        } else {
                            // 不存在有效视频, 查看是否存在正在连接的视频流
                            boolean hasConnectingStream = streamsInDb.stream().anyMatch(s -> s.getStatus() == 0);
                            if (hasConnectingStream) {
                                // 如果存在正在连接的视频流，那么休眠重试
                                Thread.sleep(500);
                                retry++;
                            } else {
                                // 不存在正在连接的视频流，那么跳出循环，进行点播处理
                                break;
                            }
                        }
                    }
                    if (availableStream == null) {
                        // 如果最后仍未找到有效的视频信息，说明之前正在处理的播放请求已经失败，那么进行点播
                        res = handlePlayRequest(playRequest);
                    }
                } else {
                    res = handlePlayRequest(playRequest);
                }
                return res;
            } catch (Exception e) {
                LOGGER.error("设备通道[{}]点播失败", playRequestKey, e);
                return new PlayResponse(false, null, null);
            }
        }

        /**
         * 停止视频流（只更新数据库状态，真正的关闭动作由无人观看检测任务完成）
         *
         * @param streamId
         */
        private void stopStream(String streamId) {
            // 更新数据库媒体流信息
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
        }

        /**
         * 关闭视频流
         *
         * @param streamId
         */
        private void closeStream(String streamId) throws Exception {
            VideoStreamPO stream = videoStreamMapper.selectById(streamId);
            if (stream == null) {
                return;
            }
            // 通知媒体服务器，关闭视频流
            ZLStreamCloseResponse zlStreamCloseResponse = zlMediaKitConnection.closeStream(streamId);
            if (zlStreamCloseResponse.getCode() == 0) {
                // 删除数据库媒体流信息
                videoStreamMapper.deleteById(streamId);
                // 如果是国标设备，发送bye请求
                if (stream.getType().equals(DeviceType.GB)) {
                    String stopParams = stream.getStopParams();
                    if (StringUtils.isNotBlank(stopParams)) {
                        this.sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor().playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
                    }
                }
            }
        }

        /**
         * 关闭视频流
         *
         * @param stream
         */
        private void closeStream(VideoStreamPO stream) throws Exception {
            String streamId = stream.getId();
            // 通知媒体服务器，关闭视频流
            ZLStreamCloseResponse zlStreamCloseResponse = zlMediaKitConnection.closeStream(streamId);
            if (zlStreamCloseResponse.getCode() == 0) {
                // 删除数据库媒体流信息
                videoStreamMapper.deleteById(streamId);
                // 如果是国标设备，发送bye请求
                if (stream.getType().equals(DeviceType.GB)) {
                    String stopParams = stream.getStopParams();
                    if (StringUtils.isNotBlank(stopParams)) {
                        this.sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor().playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
                    }
                }
            }
        }

        /**
         * 处理播放请求
         */
        private PlayResponse handlePlayRequest(PlayRequest playRequest) throws Exception {
            PlayResponse playResponse;
            String playRequestKey = playRequest.getKey();
            playRequestSet.add(playRequestKey);
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // 查询数据库，确认当前通道是否存在视频流
            QueryWrapper<VideoStreamPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id", deviceId).eq("channel_id", channelId);
            Optional<VideoStreamPO> availableStream = videoStreamMapper.selectList(queryWrapper).stream().filter(m -> m.getStatus() == 1).findFirst();
            if (!availableStream.isPresent()) {
                String deviceType = device.getType();
                switch (deviceType) {
                    case DeviceType.GB:
                        playResponse = handleGbPlayRequest(playRequest);
                        break;
                    case DeviceType.RTSP:
                        playResponse = handleRtspPlayRequest(playRequest);
                        break;
                    default:
                        throw new ManualRollbackException("设备类型[" + deviceType + "]非法");
                }
            } else {
                playResponse = new PlayResponse(true, availableStream.get().getId(), availableStream.get().getFlv());
            }
            playRequestSet.remove(playRequestKey);
            return playResponse;
        }

        /**
         * 处理国标播放请求
         *
         * @param playRequest
         */
        private PlayResponse handleGbPlayRequest(PlayRequest playRequest) throws Exception {
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // 点播视频流
            SipMediaCommandExecutor sipMediaCommandExecutor = sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor();
            String ssrc = sipServer.getSipSSRCManager().generatePlaySSRC();
            SipMediaPlayParam param = new SipMediaPlayParam(deviceId, device.getRemoteIp() + ":" + device.getRemotePort(),
                    channelId, device.getCommandTransport(), ssrc, mediaProperties.getIp(), mediaProperties.getRtpPort());
            sipMediaCommandExecutor.playStart(param);
            String streamId = HexUtils.int2Hex(Integer.parseInt(ssrc));
            // 保存视频流信息
            VideoStreamPO newMediaStream = new VideoStreamPO();
            newMediaStream.setId(streamId);
            newMediaStream.setDeviceId(deviceId);
            newMediaStream.setChannelId(channelId);
            newMediaStream.setType(device.getType());
            newMediaStream.setSsrc(ssrc);
            String flvUrl = "ws://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/rtp/" + streamId + ".flv";
            newMediaStream.setFlv(flvUrl);
            newMediaStream.setStatus(0);
            newMediaStream.setCreatedAt(DateUtils.getNowDateTimeStr());
            videoStreamMapper.insert(newMediaStream);
            // 轮询查询媒体服务器，查看streamId对应的视频流是否存在
            int retry = 0;
            while (retry < 10) {
                ZLMediaListResponse mediaStreams = zlMediaKitConnection.queryStreamList();
                boolean success = mediaStreams != null && mediaStreams.getData() != null && mediaStreams.getData().stream().anyMatch(stream -> stream.getStream().equals(streamId));
                if (success) {
                    // 更新数据库中媒体流信息
                    VideoStreamPO updateMediaStream = new VideoStreamPO();
                    updateMediaStream.setId(streamId);
                    updateMediaStream.setStatus(1);
                    videoStreamMapper.updateById(updateMediaStream);
                    return new PlayResponse(true, streamId, flvUrl);
                } else {
                    Thread.sleep(500);
                    retry++;
                }
            }
            // 点播失败，更新数据库中媒体流信息，发送bye断开连接
            VideoStreamPO deletingMediaStream = videoStreamMapper.selectById(streamId);
            String stopParams = deletingMediaStream.getStopParams();
            if (StringUtils.isNotBlank(stopParams)) {
                // 如果存在stopParams，说明点播时sip响应成功，那么发送bye请求
                sipMediaCommandExecutor.playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
            }
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
            return new PlayResponse(false, null, null);
        }

        /**
         * 处理rtsp播放请求
         *
         * @param playRequest
         */
        private PlayResponse handleRtspPlayRequest(PlayRequest playRequest) throws Exception {
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // 拉流
            String app = "rtsp";
            String streamId = HexUtils.long2Hex(System.nanoTime());
            String url = channel.getRtsp();
            ZLStreamAddStreamProxyResponse zlStreamAddStreamProxyResponse = zlMediaKitConnection.addRtspStreamProxy(app, streamId, url);
            if (zlStreamAddStreamProxyResponse.getCode() == 1) {
                throw new IllegalStateException("媒体服务器添加rtsp拉流代理失败");
            }
            // 保存视频流信息
            VideoStreamPO newMediaStream = new VideoStreamPO();
            newMediaStream.setId(streamId);
            newMediaStream.setDeviceId(deviceId);
            newMediaStream.setChannelId(channelId);
            newMediaStream.setType(device.getType());
            String flvUrl = "ws://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/" + app + "/" + streamId + ".flv";
            newMediaStream.setFlv(flvUrl);
            newMediaStream.setStatus(0);
            newMediaStream.setCreatedAt(DateUtils.getNowDateTimeStr());
            videoStreamMapper.insert(newMediaStream);
            // 轮询查询媒体服务器，查看streamId对应的视频流是否存在
            int retry = 0;
            while (retry < 10) {
                ZLMediaListResponse mediaStreams = zlMediaKitConnection.queryStreamList();
                boolean success = mediaStreams != null && mediaStreams.getData() != null && mediaStreams.getData().stream().anyMatch(stream -> stream.getStream().equals(streamId));
                if (success) {
                    // 更新数据库中媒体流信息
                    VideoStreamPO updateMediaStream = new VideoStreamPO();
                    updateMediaStream.setId(streamId);
                    updateMediaStream.setStatus(1);
                    videoStreamMapper.updateById(updateMediaStream);
                    return new PlayResponse(true, streamId, flvUrl);
                } else {
                    Thread.sleep(500);
                    retry++;
                }
            }
            // 点播失败，更新数据库中媒体流信息
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
            return new PlayResponse(false, null, null);
        }

        /**
         * 确认视频流是否有效
         *
         * @param mediaStreams
         * @return
         */
        private VideoStreamPO confirmMediaStreams(List<VideoStreamPO> mediaStreams) {
            try {
                // 请求ZLMedia媒体服务器，查询视频流列表
                ZLMediaListResponse zlMediaListResponse = zlMediaKitConnection.queryStreamList();
                List<VideoStreamPO> availableMediaStreams = new ArrayList<>();
                // 与数据库中视频流进行对比，判断是否有效
                for (VideoStreamPO mediaStream : mediaStreams) {
                    boolean available = zlMediaListResponse != null
                            && zlMediaListResponse.getData() != null
                            && zlMediaListResponse.getData().stream().anyMatch(m -> mediaStream.getId().equals(m.getStream()) && mediaStream.getStatus() == 1);
                    if (available) {
                        availableMediaStreams.add(mediaStream);
                    } else {
                        // 关闭无效视频流
                        stopStream(mediaStream.getId());
                    }
                }
                return availableMediaStreams.size() > 0 ? availableMediaStreams.get(0) : null;
            } catch (Exception e) {
                throw new ManualRollbackException("请求媒体服务器视频流列表失败", e);
            }
        }
    }

}
