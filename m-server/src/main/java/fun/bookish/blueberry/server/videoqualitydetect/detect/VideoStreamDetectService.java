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
     * ???????????????????????????
     *
     * @param deviceId
     * @param channelId
     * @param time
     * @return
     */
    public VideoStreamQualityDetectResult detectStreamQuality(String deviceId, String channelId, String time) {
        DevicePO device = deviceService.queryById(deviceId);
        if (device == null) {
            throw new ManualRollbackException("??????[" + deviceId + "]?????????");
        }
        ChannelPO channel = channelService.queryByIdAndDeviceId(channelId, deviceId);
        if (channel == null) {
            throw new ManualRollbackException("??????[" + channelId + "]?????????");
        }
        // ?????????????????????????????????????????????
        List<VideoQualityDetectArithmeticPO> arithmeticPOS = videoQualityDetectArithmeticService
                .query()
                .eq("enable", 1)
                .orderByAsc("priority")
                .list();
        return detectStreamQuality(device, channel, time, arithmeticPOS);
    }

    /**
     * ???????????????????????????
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
            // todo ????????????????????????byte?????????????????????Mat??????????????????????????????????????????????????????????????????????????????????????????
            // ????????????????????????
            detectResult = doQualityDetect(deviceId, channelId, imagePath, arithmeticPOS, time);
            // ??????????????????
            return detectResult;
        } catch (Exception e) {
            LOGGER.error("????????????????????????. device:{}", deviceKey, e);
            detectResult.setHasError(true);
            detectResult.setError(e.getMessage());
            detectResult.setDeviceId(deviceId);
            detectResult.setChannelId(channelId);
            return detectResult;
        }
    }

    /**
     * ????????????????????????
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
        // ?????????????????????????????????????????????????????????????????????
        if (arithmetics.size() == 0) {
            return res;
        }
        List<VideoQualityDetectRecordPO> detectRecords = new ArrayList<>();
        Mat image = opencv_imgcodecs.imread(imagePath);
        for (VideoQualityDetectArithmeticPO arithmetic : arithmetics) {
            String arithmeticCode = arithmetic.getCode();
            String arithmeticName = arithmetic.getName();
            try {
                // ?????????????????????????????????????????????????????????????????????????????????
                Integer applyAll = arithmetic.getApplyAll();
                if (applyAll == 0 && videoQualityDetectArithmeticApplyDeviceService.query().eq("arithmetic_id", arithmetic.getId()).eq("device_id", deviceId).eq("channel_id", channelId).count() == 0) {
                    continue;
                }
                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                JsonObject arithmeticSettings = decodeArithmeticSettings(arithmetic);
                if ("COVER".equals(arithmeticCode)) {
                    // ????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    CoverDetector coverDetector = threshold == null ? new CoverDetector() : new CoverDetector(threshold.getAsFloat());
                    CoverDetector.Result coverDetectResult = coverDetector.detect(image);
                    // ??????????????????
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
                    // ????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    NoiseDetector noiseDetector = threshold == null ? new NoiseDetector() : new NoiseDetector(threshold.getAsFloat());
                    NoiseDetector.Result noiseDetectResult = noiseDetector.detect(image);
                    // ??????????????????
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
                    // ????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    StripeDetector stripeDetector = threshold == null ? new StripeDetector() : new StripeDetector(threshold.getAsFloat());
                    StripeDetector.Result stripeDetectResult = stripeDetector.detect(image);
                    // ??????????????????
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
                        LOGGER.warn("(!)??????????????????. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("SHARPNESS".equals(arithmeticCode)) {
                    // ???????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    SharpnessDetector sharpnessDetector = threshold == null ? new SharpnessDetector() : new SharpnessDetector(threshold.getAsFloat());
                    SharpnessDetector.Result sharpnessDetectResult = sharpnessDetector.detect(image);
                    // ??????????????????
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
                        LOGGER.warn("(!)??????????????????. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("BRIGHTNESS".equals(arithmeticCode)) {
                    // ????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    BrightnessDetector brightnessDetector = threshold == null ? new BrightnessDetector() : new BrightnessDetector(threshold.getAsFloat());
                    BrightnessDetector.Result brightnessDetectResult = brightnessDetector.detect(image);
                    // ??????????????????
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
                        LOGGER.warn("(!)??????????????????. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("COLOR_CAST".equals(arithmeticCode)) {
                    // ????????????
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    ColorCastDetector colorCastDetector = threshold == null ? new ColorCastDetector() : new ColorCastDetector(threshold.getAsFloat());
                    ColorCastDetector.Result colorCastDetectResult = colorCastDetector.detect(image);
                    // ??????????????????
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
                        LOGGER.warn("(!)??????????????????. qualityError: {}, deviceId: {}, channelId: {}", qualityError, deviceId, channelId);
                        break;
                    } else {
                        detectRecord.setHasQualityError(0);
                        detectRecords.add(detectRecord);
                    }
                }
                if ("ANGLE_CHANGE".equals(arithmeticCode)) {
                    // todo ??????????????????
                }
            } catch (Exception e) {
                LOGGER.error("(x)????????????????????????. arithmeticName: {}, deviceId: {}, channelId: {}", arithmeticName, deviceId, channelId, e);
                // ??????????????????
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
     * ??????????????????
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
            LOGGER.error("????????????????????????????????????. code:{}, name:{}, settings: {}", arithmetic.getCode(), arithmetic.getName(), settings, e);
            return new JsonObject();
        }
    }

}
