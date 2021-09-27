package fun.bookish.blueberry.opencv.detector;


import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 亮度检测器
 */
public class BrightnessDetector {

    private float threshold = 0f;
    private int factor = 128;

    public BrightnessDetector() {
    }

    public BrightnessDetector(float threshold) {
        this.threshold = threshold;
    }

    public BrightnessDetector(float threshold, int factor) {
        this(threshold);
        this.factor = factor;
    }

    private float[] calculate(Mat image) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(image, gray, opencv_imgproc.COLOR_BGR2GRAY);
        UByteRawIndexer indexer = gray.createIndexer();
        float a = 0f;
        int[] hist = new int[256];
        for (int i = 0; i < 256; i++) {
            hist[i] = 0;
        }
        for (int i = 0; i < gray.rows(); i++) {
            for (int j = 0; j < gray.cols(); j++) {
                int x = indexer.get(i, j);
                a += (float) (x - factor);//在计算过程中，考虑128为亮度均值点，统计偏离的总数
                hist[x]++; //统计每个亮度的次数
            }
        }
        float da = a / (float) (gray.rows() * gray.cols());
        float D = Math.abs(da);
        float Ma = 0;
        for (int i = 0; i < 256; i++) {
            Ma += Math.abs(i - factor - da) * hist[i];
        }
        Ma /= (float) (gray.rows() * gray.cols());
        float M = Math.abs(Ma);
        float K = D / M;
        float[] res = {K, da};
        return res;
    }

    private Result doDetect(Mat image) {
        float[] calculate = calculate(image);
        boolean hasError = false;
        ErrorType errorType = ErrorType.NONE;
        if (calculate[0] > 1f) {
            hasError = true;
            errorType = calculate[1] > threshold ? ErrorType.OVER_BRIGHT : ErrorType.OVER_DARK;
        }
        return new Result(threshold, calculate[1], hasError, errorType);
    }

    public Result detect(Mat image) {
        return doDetect(image);
    }

    public Result detect(String imagePath) {
        Mat image = opencv_imgcodecs.imread(imagePath);
        return doDetect(image);
    }

    public static class Result {
        private final float threshold;
        private final float result;
        private final boolean hasError;
        private final ErrorType errorType;

        Result(float threshold, float result, boolean hasError, ErrorType errorType) {
            this.threshold = threshold;
            this.result = result;
            this.hasError = hasError;
            this.errorType = errorType;
        }

        public float getThreshold() {
            return threshold;
        }

        public float getResult() {
            return result;
        }

        public boolean isHasError() {
            return hasError;
        }

        public ErrorType getError() {
            return errorType;
        }
    }

    public enum ErrorType {
        NONE,
        OVER_BRIGHT,
        OVER_DARK;
    }

}
