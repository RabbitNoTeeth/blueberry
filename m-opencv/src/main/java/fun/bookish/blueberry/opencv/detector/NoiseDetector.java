package fun.bookish.blueberry.opencv.detector;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import static org.bytedeco.opencv.global.opencv_core.BORDER_DEFAULT;


/**
 * 噪声检测器
 */
public class NoiseDetector {

    private static final double MAX_DIFF = 50;

    private float threshold = 20.0f;

    public NoiseDetector() {
    }

    public NoiseDetector(float threshold) {
        this.threshold = threshold;
    }

    private Mat calcGrads(Mat image) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(image, gray, opencv_imgproc.COLOR_BGR2GRAY);
        Mat grads_x = new Mat();
        Mat grads_y = new Mat();
        opencv_imgproc.Sobel(gray, grads_x, opencv_core.CV_64F, 1, 0, 3, 1, 0, BORDER_DEFAULT);
        opencv_imgproc.Sobel(gray, grads_y, opencv_core.CV_64F, 0, 1, 3, 1, 0, BORDER_DEFAULT);
        return opencv_core.add(opencv_core.abs(grads_x), opencv_core.abs(grads_y)).asMat();
    }

    private double calcDiffEnergy(Mat image, Mat blur) {
        Mat energy1 = calcGrads(image);
        Mat energy2 = calcGrads(blur);
        int image_h = image.rows();
        int image_w = image.cols();
        int image_c = image.channels();
        double sub = opencv_core.sumElems(opencv_core.subtract(energy1, energy2).asMat()).get() / (image_h * image_w * image_c);
        return sub;
    }

    private Result doDetect(Mat image) {
        Mat blur = new Mat();
        opencv_imgproc.medianBlur(image, blur, 3);
        double diff = calcDiffEnergy(image, blur);
        if (diff > MAX_DIFF) {
            diff = MAX_DIFF;
        }
        float level = (float) (diff * 100 / MAX_DIFF);
        return new Result(threshold, level, level > threshold);
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
