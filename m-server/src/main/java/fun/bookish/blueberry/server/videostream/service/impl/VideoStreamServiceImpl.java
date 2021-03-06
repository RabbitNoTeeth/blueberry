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
 * ???????????? ???????????????
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
     * ?????????????????????
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
     * ??????????????????
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public VideoStreamPlayResponse play(String deviceId, String channelId) throws Exception {
        // ????????????????????????
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("??????" + deviceId + "?????????");
        }
        // ??????channel????????????
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("??????" + channelId + "?????????");
        }
        // ???????????????????????????????????????
        PlayResponse playResponse = videoStreamManager.playStream(new PlayRequest(device, channel));
        if (playResponse.isSuccess()) {
            VideoStreamPlayResponse res = new VideoStreamPlayResponse();
            res.setDeviceId(deviceId);
            res.setChannelId(channelId);
            res.setFlv(playResponse.getFlv());
            return res;
        } else {
            throw new ManualRollbackException("????????????");
        }
    }

    /**
     * ?????????????????????
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
        // ????????????????????????
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("??????" + deviceId + "?????????");
        }
        // ??????channel????????????
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("??????" + channelId + "?????????");
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
        // ??????????????????
        IVideoStreamService.PlayResponse playResponse = playStream(new IVideoStreamService.PlayRequest(device, channel));
        if (!playResponse.isSuccess()) {
            throw new IllegalStateException("??????????????????");
        }
        // ????????????????????????????????????
        String flv = playResponse.getFlv();
        byte[] snapBytes = zlMediaKitConnection.querySnap(flv.replace("ws://", "http://"));
        // todo ???????????????(?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????)
        // ?????????????????????
        String saveDir = "/" + deviceId + "/" + channelId;
        String savePath = mediaProperties.getSnapshotSavaPath() + saveDir;
        String snapName = (StringUtils.isBlank(time) ? DateUtils.getNowDateTimeStr() : time) + ".jpeg";
        FileUtils.save(savePath, snapName, snapBytes);
        // ??????????????????
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
         * ????????????set?????????????????????
         */
        private final Set<String> playRequestSet = new ConcurrentSkipListSet<>();

        private VideoStreamManager(VideoStreamMapper videoStreamMapper, MediaProperties mediaProperties, SipServer sipServer, ZLMediaKitConnection zlMediaKitConnection) {
            this.videoStreamMapper = videoStreamMapper;
            this.mediaProperties = mediaProperties;
            this.sipServer = sipServer;
            this.zlMediaKitConnection = zlMediaKitConnection;
        }

        /**
         * ???????????????
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
                    // playRequestSet????????????playRequestKey???????????????????????????????????????????????????????????????
                    int retry = 0;
                    while (retry < 5) {
                        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        QueryWrapper<VideoStreamPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("device_id", deviceId).eq("channel_id", channelId);
                        List<VideoStreamPO> streamsInDb = videoStreamMapper.selectList(queryWrapper);
                        availableStream = confirmMediaStreams(streamsInDb);
                        if (availableStream != null) {
                            // ???????????????????????????????????????
                            res = new PlayResponse(true, availableStream.getId(), availableStream.getFlv());
                            break;
                        } else {
                            // ?????????????????????, ??????????????????????????????????????????
                            boolean hasConnectingStream = streamsInDb.stream().anyMatch(s -> s.getStatus() == 0);
                            if (hasConnectingStream) {
                                // ?????????????????????????????????????????????????????????
                                Thread.sleep(500);
                                retry++;
                            } else {
                                // ???????????????????????????????????????????????????????????????????????????
                                break;
                            }
                        }
                    }
                    if (availableStream == null) {
                        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        res = handlePlayRequest(playRequest);
                    }
                } else {
                    res = handlePlayRequest(playRequest);
                }
                return res;
            } catch (Exception e) {
                LOGGER.error("????????????[{}]????????????", playRequestKey, e);
                return new PlayResponse(false, null, null);
            }
        }

        /**
         * ??????????????????????????????????????????????????????????????????????????????????????????????????????
         *
         * @param streamId
         */
        private void stopStream(String streamId) {
            // ??????????????????????????????
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
        }

        /**
         * ???????????????
         *
         * @param streamId
         */
        private void closeStream(String streamId) throws Exception {
            VideoStreamPO stream = videoStreamMapper.selectById(streamId);
            if (stream == null) {
                return;
            }
            // ???????????????????????????????????????
            ZLStreamCloseResponse zlStreamCloseResponse = zlMediaKitConnection.closeStream(streamId);
            if (zlStreamCloseResponse.getCode() == 0) {
                // ??????????????????????????????
                videoStreamMapper.deleteById(streamId);
                // ??????????????????????????????bye??????
                if (stream.getType().equals(DeviceType.GB)) {
                    String stopParams = stream.getStopParams();
                    if (StringUtils.isNotBlank(stopParams)) {
                        this.sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor().playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
                    }
                }
            }
        }

        /**
         * ???????????????
         *
         * @param stream
         */
        private void closeStream(VideoStreamPO stream) throws Exception {
            String streamId = stream.getId();
            // ???????????????????????????????????????
            ZLStreamCloseResponse zlStreamCloseResponse = zlMediaKitConnection.closeStream(streamId);
            if (zlStreamCloseResponse.getCode() == 0) {
                // ??????????????????????????????
                videoStreamMapper.deleteById(streamId);
                // ??????????????????????????????bye??????
                if (stream.getType().equals(DeviceType.GB)) {
                    String stopParams = stream.getStopParams();
                    if (StringUtils.isNotBlank(stopParams)) {
                        this.sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor().playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
                    }
                }
            }
        }

        /**
         * ??????????????????
         */
        private PlayResponse handlePlayRequest(PlayRequest playRequest) throws Exception {
            PlayResponse playResponse;
            String playRequestKey = playRequest.getKey();
            playRequestSet.add(playRequestKey);
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // ?????????????????????????????????????????????????????????
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
                        throw new ManualRollbackException("????????????[" + deviceType + "]??????");
                }
            } else {
                playResponse = new PlayResponse(true, availableStream.get().getId(), availableStream.get().getFlv());
            }
            playRequestSet.remove(playRequestKey);
            return playResponse;
        }

        /**
         * ????????????????????????
         *
         * @param playRequest
         */
        private PlayResponse handleGbPlayRequest(PlayRequest playRequest) throws Exception {
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // ???????????????
            SipMediaCommandExecutor sipMediaCommandExecutor = sipServer.getSipCommandExecutorManager().getSipMediaCommandExecutor();
            String ssrc = sipServer.getSipSSRCManager().generatePlaySSRC();
            SipMediaPlayParam param = new SipMediaPlayParam(deviceId, device.getRemoteIp() + ":" + device.getRemotePort(),
                    channelId, device.getCommandTransport(), ssrc, mediaProperties.getIp(), mediaProperties.getRtpPort());
            sipMediaCommandExecutor.playStart(param);
            String streamId = HexUtils.int2Hex(Integer.parseInt(ssrc));
            // ?????????????????????
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
            // ????????????????????????????????????streamId??????????????????????????????
            int retry = 0;
            while (retry < 10) {
                ZLMediaListResponse mediaStreams = zlMediaKitConnection.queryStreamList();
                boolean success = mediaStreams != null && mediaStreams.getData() != null && mediaStreams.getData().stream().anyMatch(stream -> stream.getStream().equals(streamId));
                if (success) {
                    // ?????????????????????????????????
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
            // ?????????????????????????????????????????????????????????bye????????????
            VideoStreamPO deletingMediaStream = videoStreamMapper.selectById(streamId);
            String stopParams = deletingMediaStream.getStopParams();
            if (StringUtils.isNotBlank(stopParams)) {
                // ????????????stopParams??????????????????sip???????????????????????????bye??????
                sipMediaCommandExecutor.playStop(JsonUtils.decode(stopParams, SipMediaStopParam.class));
            }
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
            return new PlayResponse(false, null, null);
        }

        /**
         * ??????rtsp????????????
         *
         * @param playRequest
         */
        private PlayResponse handleRtspPlayRequest(PlayRequest playRequest) throws Exception {
            DevicePO device = playRequest.getDevice();
            String deviceId = device.getId();
            ChannelPO channel = playRequest.getChannel();
            String channelId = channel.getId();
            // ??????
            String app = "rtsp";
            String streamId = HexUtils.long2Hex(System.nanoTime());
            String url = channel.getRtsp();
            ZLStreamAddStreamProxyResponse zlStreamAddStreamProxyResponse = zlMediaKitConnection.addRtspStreamProxy(app, streamId, url);
            if (zlStreamAddStreamProxyResponse.getCode() == 1) {
                throw new IllegalStateException("?????????????????????rtsp??????????????????");
            }
            // ?????????????????????
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
            // ????????????????????????????????????streamId??????????????????????????????
            int retry = 0;
            while (retry < 10) {
                ZLMediaListResponse mediaStreams = zlMediaKitConnection.queryStreamList();
                boolean success = mediaStreams != null && mediaStreams.getData() != null && mediaStreams.getData().stream().anyMatch(stream -> stream.getStream().equals(streamId));
                if (success) {
                    // ?????????????????????????????????
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
            // ????????????????????????????????????????????????
            VideoStreamPO updateMediaStream = new VideoStreamPO();
            updateMediaStream.setId(streamId);
            updateMediaStream.setStatus(2);
            videoStreamMapper.updateById(updateMediaStream);
            return new PlayResponse(false, null, null);
        }

        /**
         * ???????????????????????????
         *
         * @param mediaStreams
         * @return
         */
        private VideoStreamPO confirmMediaStreams(List<VideoStreamPO> mediaStreams) {
            try {
                // ??????ZLMedia???????????????????????????????????????
                ZLMediaListResponse zlMediaListResponse = zlMediaKitConnection.queryStreamList();
                List<VideoStreamPO> availableMediaStreams = new ArrayList<>();
                // ?????????????????????????????????????????????????????????
                for (VideoStreamPO mediaStream : mediaStreams) {
                    boolean available = zlMediaListResponse != null
                            && zlMediaListResponse.getData() != null
                            && zlMediaListResponse.getData().stream().anyMatch(m -> mediaStream.getId().equals(m.getStream()) && mediaStream.getStatus() == 1);
                    if (available) {
                        availableMediaStreams.add(mediaStream);
                    } else {
                        // ?????????????????????
                        stopStream(mediaStream.getId());
                    }
                }
                return availableMediaStreams.size() > 0 ? availableMediaStreams.get(0) : null;
            } catch (Exception e) {
                throw new ManualRollbackException("??????????????????????????????????????????", e);
            }
        }
    }

}
