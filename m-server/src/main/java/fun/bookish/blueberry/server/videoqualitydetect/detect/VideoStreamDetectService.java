package fun.bookish.blueberry.server.videoqualitydetect.detect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.opencv.detector.*;
import fun.bookish.blueberry.server.channel.entity.ChannelPO;
import fun.bookish.blueberry.server.channel.service.IChannelService;
import fun.bookish.blueberry.server.device.entity.DevicePO;
import fun.bookish.blueberry.server.device.service.IDeviceService;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticPO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import fun.bookish.blueberry.server.videoqualitydetect.arithmeticapplydevice.service.IVideoQualityDetectArithmeticApplyDeviceService;
import fun.bookish.blueberry.server.videoqualitydetect.detect.entity.VideoStreamQualityDetectResult;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.service.IVideoQualityDetectRecordService;
import fun.bookish.blueberry.server.videostream.service.IVideoStreamService;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoStreamDetectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoStreamDetectService.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private IVideoStreamService videoStreamService;
    @Autowired
    private IVideoQualityDetectRecordService videoQualityDetectRecordService;
    @Autowired
    private IVideoQualityDetectArithmeticService videoQualityDetectArithmeticService;
    @Autowired
    private IVideoQualityDetectArithmeticApplyDeviceService videoQualityDetectArithmeticApplyDeviceService;

    /**
     * 检测视频流图像质量
     *
     * @param deviceId
     * @param channelId
     * @param time
     * @return
     */
    public VideoStreamQualityDetectResult detectStreamQuality(String deviceId, String channelId, String time) {
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("设备[" + deviceId + "]不存在");
        }
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("通道[" + channelId + "]不存在");
        }
        // 查询所有启用的视频质量检测算法
        List<VideoQualityDetectArithmeticPO> arithmeticPOS = videoQualityDetectArithmeticService
                .query()
                .eq("enable", 1)
                .orderByAsc("priority")
                .list();
        return detectStreamQuality(device, channel, time, arithmeticPOS);
    }

    /**
     * 检测视频流图像质量
     *
     * @param device
     * @param channel
     * @param time
     * @param arithmeticPOS
     * @return
     */
    public VideoStreamQualityDetectResult detectStreamQuality(DevicePO device, ChannelPO channel, String time, List<VideoQualityDetectArithmeticPO> arithmeticPOS) {
        String deviceId = device.getId();
        String channelId = channel.getId();
        String deviceKey = deviceId + "@" + channelId;
        VideoStreamQualityDetectResult detectResult = new VideoStreamQualityDetectResult();
        String imagePath = null;
        try {
            imagePath = videoStreamService.takeSnapshot(device, channel, time).getSavePath();
            // todo 暂时未找到直接用byte数组构建出正确Mat对象的方法，后续可以优化，这样便不需要多读取一次本地图像文件
            // 进行图片质量检测
            detectResult = doQualityDetect(deviceId, channelId, imagePath, arithmeticPOS, time);
            // 返回检测结果
            return detectResult;
        } catch (Exception e) {
            LOGGER.error("视频质量检测异常. device:{}", deviceKey, e);
            detectResult.setHasError(true);
            detectResult.setError(e.getMessage());
            detectResult.setDeviceId(deviceId);
            detectResult.setChannelId(channelId);
            return detectResult;
        }
    }

    /**
     * 进行视频质量检测
     *
     * @param deviceId
     * @param channelId
     * @param imagePath
     * @param arithmetics
     * @param time
     * @return
     */
    private VideoStreamQualityDetectResult doQualityDetect(String deviceId, String channelId, String imagePath, List<VideoQualityDetectArithmeticPO> arithmetics, String time) {
        VideoStreamQualityDetectResult res = new VideoStreamQualityDetectResult();
        res.setHasError(false);
        res.setHasQualityError(false);
        // 如果不存在启用的算法，直接返回，不需要进行检测
        if (arithmetics.size() == 0) {
            return res;
        }
        List<VideoQualityDetectRecordPO> detectRecords = new ArrayList<>();
        Mat image = opencv_imgcodecs.imread(imagePath);
        for (VideoQualityDetectArithmeticPO arithmetic : arithmetics) {
            String arithmeticCode = arithmetic.getCode();
            String arithmeticName = arithmetic.getName();
            try {
                // 判断算法是否应用于当前设备，如果未应用，直接跳过该算法
                Integer applyAll = arithmetic.getApplyAll();
                if (applyAll == 0 && videoQualityDetectArithmeticApplyDeviceService.query().eq("arithmetic_id", arithmetic.getId()).eq("device_id", deviceId).eq("channel_id", channelId).count() == 0) {
                    continue;
                }
                // 采用短路模式，按算法优先级进行质量检测，检测出异常直接停止后续检测
                JsonObject arithmeticSettings = decodeArithmeticSettings(arithmetic);
                if ("COVER".equals(arithmeticCode)) {
                    // 噪声检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    CoverDetector coverDetector = threshold == null ? new CoverDetector() : new CoverDetector(threshold.getAsFloat());
                    CoverDetector.Result coverDetectResult = coverDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(coverDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (coverDetectResult.isHasError()) {
                        String qualityError = "ERROR_COVER";
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("NOISE".equals(arithmeticCode)) {
                    // 噪声检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    NoiseDetector noiseDetector = threshold == null ? new NoiseDetector() : new NoiseDetector(threshold.getAsFloat());
                    NoiseDetector.Result noiseDetectResult = noiseDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(noiseDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (noiseDetectResult.isHasError()) {
                        String qualityError = "ERROR_NOISE";
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("STRIPE".equals(arithmeticCode)) {
                    // 条纹检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    StripeDetector stripeDetector = threshold == null ? new StripeDetector() : new StripeDetector(threshold.getAsFloat());
                    StripeDetector.Result stripeDetectResult = stripeDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(stripeDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (stripeDetectResult.isHasError()) {
                        String qualityError = "ERROR_STRIPE";
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        LOGGER.warn("(!)视频质量异常. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("SHARPNESS".equals(arithmeticCode)) {
                    // 清晰度检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    SharpnessDetector sharpnessDetector = threshold == null ? new SharpnessDetector() : new SharpnessDetector(threshold.getAsFloat());
                    SharpnessDetector.Result sharpnessDetectResult = sharpnessDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(sharpnessDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (sharpnessDetectResult.isHasError()) {
                        String qualityError = "ERROR_SHARPNESS";
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        LOGGER.warn("(!)视频质量异常. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("BRIGHTNESS".equals(arithmeticCode)) {
                    // 亮度检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    BrightnessDetector brightnessDetector = threshold == null ? new BrightnessDetector() : new BrightnessDetector(threshold.getAsFloat());
                    BrightnessDetector.Result brightnessDetectResult = brightnessDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(brightnessDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (brightnessDetectResult.isHasError()) {
                        String qualityError = "ERROR_SHARPNESS";
                        BrightnessDetector.ErrorType errorType = brightnessDetectResult.getError();
                        switch (errorType) {
                            case OVER_BRIGHT:
                                qualityError += "_OVER_LIGHT";
                                break;
                            case OVER_DARK:
                                qualityError += "_OVER_DARK";
                                break;
                            default:
                                break;
                        }
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        LOGGER.warn("(!)视频质量异常. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("COLOR_CAST".equals(arithmeticCode)) {
                    // 偏色检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    ColorCastDetector colorCastDetector = threshold == null ? new ColorCastDetector() : new ColorCastDetector(threshold.getAsFloat());
                    ColorCastDetector.Result colorCastDetectResult = colorCastDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setDeviceId(deviceId);
                    detectRecord.setChannelId(channelId);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(colorCastDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (colorCastDetectResult.isHasError()) {
                        String qualityError = "ERROR_COLOR_CAST";
                        ColorCastDetector.ErrorType errorType = colorCastDetectResult.getErrorType();
                        switch (errorType) {
                            case OVER_BLUE:
                                qualityError += "_OVER_BLUE";
                                break;
                            case OVER_RED:
                                qualityError += "_OVER_RED";
                                break;
                            case OVER_GREEN:
                                qualityError += "_OVER_GREEN";
                                break;
                            case OVER_YELLOW:
                                qualityError += "_OVER_YELLOW";
                                break;
                            default:
                                break;
                        }
                        detectRecord.setHasQualityError(1);
                        detectRecord.setQualityError(qualityError);
                        detectRecords.add(detectRecord);
                        res.setHasError(false);
                        res.setHasQualityError(true);
                        res.setQualityError(qualityError);
                        LOGGER.warn("(!)视频质量异常. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("ANGLE_CHANGE".equals(arithmeticCode)) {
                    // todo 角度变化检测
                }
            } catch (Exception e) {
                LOGGER.error("(x)视频质量检测异常. arithmeticName: {}, deviceId: {}, channelId: {}", arithmeticName, deviceId, channelId, e);
                // 创建检测记录
                VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                detectRecord.setArithmeticCode(arithmeticCode);
                detectRecord.setArithmeticName(arithmeticName);
                detectRecord.setDeviceId(deviceId);
                detectRecord.setChannelId(channelId);
                detectRecord.setImagePath(imagePath);
                detectRecord.setHasError(1);
                detectRecord.setError(e.getMessage());
                detectRecord.setCreatedAt(time);
                detectRecords.add(detectRecord);
            }
        }
        if (detectRecords.size() > 0) {
            videoQualityDetectRecordService.saveBatch(detectRecords);
        }
        return res;
    }

    /**
     * 解码算法参数
     *
     * @param arithmetic
     * @return
     */
    private JsonObject decodeArithmeticSettings(VideoQualityDetectArithmeticPO arithmetic) {
        String settings = arithmetic.getSettings();
        try {
            if (StringUtils.isBlank(settings)) {
                return new JsonObject();
            }
            return JsonUtils.decodeToJsonObject(settings);
        } catch (Exception e) {
            LOGGER.error("视频质量算法参数解析失败. code:{}, name:{}, settings: {}", arithmetic.getCode(), arithmetic.getName(), settings, e);
            return new JsonObject();
        }
    }

}
