package fun.bookish.blueberry.opencv.detector;


import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 条纹检测器
 */
public class CoverDetector {

    private float threshold = 0.50f;

    public CoverDetector() {
    }

    public CoverDetector(float threshold) {
        this.threshold = threshold;
    }

    private Result doDetect(Mat image) {
        Mat rgbImage = new Mat();
        opencv_imgproc.cvtColor(image, rgbImage, opencv_imgproc.COLOR_BGR2RGB);
        Mat grayImage = new Mat();
        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_RGB2GRAY);
        Mat blur = new Mat();
        opencv_imgproc.medianBlur(grayImage, blur, 9);
        Mat thresholdImage = new Mat();
        opencv_imgproc.threshold(blur, thresholdImage, 127, 255, opencv_imgproc.THRESH_BINARY);
        double blackPixelCount = 0;
        UByteIndexer indexer = thresholdImage.createIndexer();
        int height = thresholdImage.rows();
        int width = thresholdImage.cols();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (indexer.get(i, j) == 0) {
                    blackPixelCount++;
                }
            }
        }
        double res = blackPixelCount / (height * width);
        return new Result(threshold, (float) res, res > threshold);
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
