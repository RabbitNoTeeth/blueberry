package fun.bookish.blueberry.server.videostream.service;

import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.videostream.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 视频流表 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-11
 */
public interface IVideoStreamService extends IService<VideoStreamPO> {

    /**
     * 查询视频流列表
     * @return
     */
    List<VideoStreamVO> queryList();

    /**
     * 实时视频点播
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    VideoStreamPlayResponse play(String deviceId, String channelId) throws Exception;

    /**
     * 停止实时视频流
     *
     * @param streamId
     * @return
     */
    VideoStreamStopResponse stop(String streamId) throws Exception;

    /**
     * 根据设备id和通道id查询视频流列表
     * @param deviceId
     * @param channelId
     * @return
     */
    List<VideoStreamPO> queryListByDeviceIdAndChannelId(String deviceId, String channelId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    VideoStreamPO queryById(String id);

    /**
     * 根据ssrc更新视频流信息
     * @param streamPO
     * @return
     */
    String updateBySSRC(VideoStreamPO streamPO);

    /**
     * 播放视频流
     * @param playRequest
     * @return
     */
    PlayResponse playStream(PlayRequest playRequest);

    /**
     * 关闭视频流
     * @param streamId
     */
    void closeStream(String streamId) throws Exception;

    /**
     * 关闭视频流
     * @param stream
     */
    void closeStream(VideoStreamPO stream) throws Exception;

    /**
     * 获取视频截图
     * @param deviceId
     * @param channelId
     * @return
     * @throws Exception
     */
    VideoStreamSnapshot takeSnapshot(String deviceId, String channelId) throws Exception;

    /**
     * 获取视频截图
     * @param deviceId
     * @param channelId
     * @return
     * @throws Exception
     */
    VideoStreamSnapshot takeSnapshot(String deviceId, String channelId, String time) throws Exception;

    /**
     * 获取视频截图
     * @param device
     * @param channel
     * @return
     * @throws Exception
     */
    VideoStreamSnapshot takeSnapshot(DevicePO device, ChannelPO channel) throws Exception;

    /**
     * 获取视频截图
     * @param device
     * @param channel
     * @return
     * @throws Exception
     */
    VideoStreamSnapshot takeSnapshot(DevicePO device, ChannelPO channel, String time) throws Exception;

    public static class PlayRequest {
        private final DevicePO device;
        private final ChannelPO channel;

        public PlayRequest(DevicePO device, ChannelPO channel) {
            this.device = device;
            this.channel = channel;
        }

        public DevicePO getDevice() {
            return device;
        }

        public ChannelPO getChannel() {
            return channel;
        }

        public String getKey() {
            return device.getId() + "@" + channel.getId();
        }
    }

    public static class PlayResponse {
        private final boolean success;
        private final String streamId;
        private final String flv;

        public PlayResponse(boolean success, String streamId, String flv) {
            this.success = success;
            this.streamId = streamId;
            this.flv = flv;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getFlv() {
            return flv;
        }

        public String getStreamId() {
            return this.streamId;
        }
    }

    public static class StopRequest {
        private final String streamId;

        public StopRequest(String streamId, ChannelPO channel) {
            this.streamId = streamId;
        }

        public String getStreamId() {
            return streamId;
        }
    }

}
