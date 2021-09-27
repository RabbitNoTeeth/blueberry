package fun.bookish.blueberry.opencv.detector;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;

/**
 * 色偏检测器
 */
public class ColorCastDetector {

    private float threshold = 2f;
    private final ColorCastDecision colorCastDecision;

    public ColorCastDetector() {
        this.colorCastDecision = new ColorCastDecision();
    }

    public ColorCastDetector(ColorCastDecision colorCastDecision) {
        this.colorCastDecision = colorCastDecision;
    }

    public ColorCastDetector(float threshold) {
        this.threshold = threshold;
        this.colorCastDecision = new ColorCastDecision();
    }

    public ColorCastDetector(float threshold, ColorCastDecision colorCastDecision) {
        this.threshold = threshold;
        this.colorCastDecision = colorCastDecision;
    }

    private Result doDetect(Mat image) {
        Mat lab = new Mat();
        opencv_imgproc.cvtColor(image, lab, opencv_imgproc.COLOR_BGR2Lab);
        MatVector labChannels = new MatVector();
        opencv_core.split(lab, labChannels);
        Mat channel_l = labChannels.get(0);
        Mat channel_a = labChannels.get(1);
        Mat channel_b = labChannels.get(2);
        int h = lab.rows();
        int w = lab.cols();
        // da > 0,偏红,否则偏绿
        double da = opencv_core.sumElems(channel_a).get() / (h * w) - 128;
        // db > 0,偏黄,否则偏蓝
        double db = opencv_core.sumElems(channel_b).get() / (h * w) - 128;
        int[] hist_a = new int[256];
        int[] hist_b = new int[256];
        UByteRawIndexer aIndexer = channel_a.createIndexer();
        UByteRawIndexer bIndexer = channel_b.createIndexer();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int ta = aIndexer.get(i, j);
                int tb = bIndexer.get(i, j);
                hist_a[ta] += 1;
                hist_b[tb] += 1;
            }
        }
        double msq_a = 0f;
        double msq_b = 0f;
        for (int i = 0; i < 256; i++) {
            msq_a += Math.abs(i - 128 - da) * hist_a[i] / (w * h);
            msq_b += Math.abs(i - 128 - db) * hist_b[i] / (w * h);
        }
        double dividend = Math.sqrt(da * da + db * db);
        double divisor = Math.sqrt(msq_a * msq_a + msq_b * msq_b);
        double res = 0f;
        ErrorType errorType = ErrorType.NONE;
        if (dividend != 0d) {
            if (divisor != 0d) {
                // 偏色因子越大，偏色越严重
                res = dividend / divisor;
                errorType = decideColorType(da, db);
            }
        }
        return new Result(threshold, res, da, db, errorType != ErrorType.NONE, errorType);
    }

    private ErrorType decideColorType(double da, double db) {
        // 根据da、db值判断具体偏色情况（红、黄、蓝、绿）
        // da > 0,偏红,否则偏绿
        // db > 0,偏黄,否则偏蓝
        if (colorCastDecision.blue(da, db)) {
            return ErrorType.OVER_BLUE;
        }
        if (colorCastDecision.red(da, db)) {
            return ErrorType.OVER_RED;
        }
        if (colorCastDecision.green(da, db)) {
            return ErrorType.OVER_GREEN;
        }
        if (colorCastDecision.yellow(da, db)) {
            return ErrorType.OVER_YELLOW;
        }
        return ErrorType.NONE;
    }

    public Result detect(Mat image) {
        return doDetect(image);
    }

    public Result detect(String imagePath) {
        Mat image = opencv_imgcodecs.imread(imagePath);
        return doDetect(image);
    }

    public static class Result {
        private final double threshold;
        private final double result;
        private final double da;
        private final double db;
        private final boolean hasError;
        private final ErrorType errorType;

        Result(double threshold, double result, double da, double db, boolean hasError, ErrorType errorType) {
            this.threshold = threshold;
            this.result = result;
            this.da = da;
            this.db = db;
            this.hasError = hasError;
            this.errorType = errorType;
        }

        public boolean isHasError() {
            return hasError;
        }

        public double getThreshold() {
            return threshold;
        }

        public double getResult() {
            return result;
        }

        public double getDa() {
            return da;
        }

        public double getDb() {
            return db;
        }

        public ErrorType getErrorType() {
            return errorType;
        }
    }

    public enum ErrorType {
        NONE,
        OVER_RED,
        OVER_GREEN,
        OVER_YELLOW,
        OVER_BLUE;
    }

    public static class ColorCastDecision {
        // da > 0,偏红,否则偏绿
        // db > 0,偏黄,否则偏蓝
        public boolean blue(double da, double db) {
            return db < -30 && -10 < da && da < 10;
        }

        public boolean red(double da, double db) {
            return da > 30 && -10 < db && db < 10;
        }

        public boolean yellow(double da, double db) {
            return db > 30 && -10 < da && da < 10;
        }

        public boolean green(double da, double db) {
            return da < -30 && -10 < db && db < 10;
        }
    }

}
