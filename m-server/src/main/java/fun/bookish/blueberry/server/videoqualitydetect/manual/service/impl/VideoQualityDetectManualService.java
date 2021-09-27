package fun.bookish.blueberry.server.videoqualitydetect.manual.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.FileUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import fun.bookish.blueberry.opencv.detector.*;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.VideoQualityDetectArithmeticPO;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service.IVideoQualityDetectArithmeticService;
import fun.bookish.blueberry.server.videoqualitydetect.manual.entity.VideoQualityDetectManualVO;
import fun.bookish.blueberry.server.videoqualitydetect.manual.service.IVideoQualityDetectManualService;
import fun.bookish.blueberry.server.videoqualitydetect.record.entity.VideoQualityDetectRecordPO;
import fun.bookish.blueberry.server.videoqualitydetect.record.service.IVideoQualityDetectRecordService;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoQualityDetectManualService implements IVideoQualityDetectManualService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoQualityDetectManualService.class);

    @Value("${web.upload}")
    private String fileUploadPath;

    @Autowired
    private IVideoQualityDetectRecordService videoQualityDetectRecordService;

    @Autowired
    private IVideoQualityDetectArithmeticService videoQualityDetectArithmeticService;

    @Override
    public VideoQualityDetectManualVO upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String path = fileUploadPath + "/images";
        String name = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
        FileUtils.save(path, name, file.getBytes());
        return doQualityDetect(path + "/" + name);
    }

    /**
     * 进行视频质量检测
     *
     * @param imagePath
     * @return
     */
    private VideoQualityDetectManualVO doQualityDetect(String imagePath) {
        String time = DateUtils.getNowDateTimeStr();
        VideoQualityDetectManualVO res = new VideoQualityDetectManualVO();
        res.setHasError(false);
        res.setHasQualityError(false);
        List<VideoQualityDetectArithmeticPO> arithmetics = videoQualityDetectArithmeticService.query().orderByAsc("priority").list();
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
                // 采用短路模式，按算法优先级进行质量检测，检测出异常直接停止后续检测
                JsonObject arithmeticSettings = decodeArithmeticSettings(arithmetic);
                if ("COVER".equals(arithmeticCode)) {
                    // 噪声检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    CoverDetector coverDetector = threshold == null ? new CoverDetector() : new CoverDetector(threshold.getAsFloat());
                    CoverDetector.Result noiseDetectResult = coverDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(noiseDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (noiseDetectResult.isHasError()) {
                        String qualityError = "遮挡异常";
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
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(noiseDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (noiseDetectResult.isHasError()) {
                        String qualityError = "噪声异常";
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
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(stripeDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (stripeDetectResult.isHasError()) {
                        String qualityError = "条纹异常";
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
                if ("SHARPNESS".equals(arithmeticCode)) {
                    // 清晰度检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    SharpnessDetector sharpnessDetector = threshold == null ? new SharpnessDetector() : new SharpnessDetector(threshold.getAsFloat());
                    SharpnessDetector.Result sharpnessDetectResult = sharpnessDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(sharpnessDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (sharpnessDetectResult.isHasError()) {
                        String qualityError = "清晰度异常";
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
                if ("BRIGHTNESS".equals(arithmeticCode)) {
                    // 亮度检测
                    JsonElement threshold = arithmeticSettings.get("threshold");
                    BrightnessDetector brightnessDetector = threshold == null ? new BrightnessDetector() : new BrightnessDetector(threshold.getAsFloat());
                    BrightnessDetector.Result brightnessDetectResult = brightnessDetector.detect(image);
                    // 创建检测记录
                    VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                    detectRecord.setArithmeticCode(arithmeticCode);
                    detectRecord.setArithmeticName(arithmeticName);
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(brightnessDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (brightnessDetectResult.isHasError()) {
                        String qualityError = "亮度异常";
                        BrightnessDetector.ErrorType errorType = brightnessDetectResult.getError();
                        switch (errorType) {
                            case OVER_BRIGHT:
                                qualityError += "[过亮]";
                                break;
                            case OVER_DARK:
                                qualityError += "[过暗]";
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
                    detectRecord.setImagePath(imagePath);
                    detectRecord.setHasError(0);
                    detectRecord.setDetail(JsonUtils.encode(colorCastDetectResult));
                    detectRecord.setCreatedAt(time);
                    if (colorCastDetectResult.isHasError()) {
                        String qualityError = "偏色异常";
                        ColorCastDetector.ErrorType errorType = colorCastDetectResult.getErrorType();
                        switch (errorType) {
                            case OVER_BLUE:
                                qualityError += "[偏蓝]";
                                break;
                            case OVER_RED:
                                qualityError += "[偏红]";
                                break;
                            case OVER_GREEN:
                                qualityError += "[偏绿]";
                                break;
                            case OVER_YELLOW:
                                qualityError += "[偏黄]";
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
                // 创建检测记录
                VideoQualityDetectRecordPO detectRecord = new VideoQualityDetectRecordPO();
                detectRecord.setArithmeticCode(arithmeticCode);
                detectRecord.setArithmeticName(arithmeticName);
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
