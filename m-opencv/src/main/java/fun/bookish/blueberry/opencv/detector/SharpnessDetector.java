package fun.bookish.blueberry.opencv.detector;

import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 清晰度检测器
 */
public class SharpnessDetector {

    private float threshold = 10.0f;

    public SharpnessDetector() {
    }

    public SharpnessDetector(float threshold) {
        this.threshold = threshold;
    }

    private Result doDetect(Mat image) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(image, gray, opencv_imgproc.COLOR_BGR2GRAY);
        Mat laplaci = new Mat();
        opencv_imgproc.Laplacian(gray, laplaci, opencv_core.CV_64F);
        Mat means = new Mat();
        Mat stddev = new Mat();
        opencv_core.meanStdDev(laplaci, means, stddev);
        DoubleRawIndexer meansIndexer = means.createIndexer();
        DoubleRawIndexer stddevIndexer = stddev.createIndexer();
        double men = meansIndexer.get(0);
        double dev = stddevIndexer.get(0);
        return new Result(threshold, (float) dev, dev < threshold);
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

        Result(float threshold, float result, boolean hasError) {
            this.threshold = threshold;
            this.result = result;
            this.hasError = hasError;
        }

        public boolean isHasError() {
            return hasError;
        }

        public float getThreshold() {
            return threshold;
        }

        public float getResult() {
            return result;
        }
    }

}
