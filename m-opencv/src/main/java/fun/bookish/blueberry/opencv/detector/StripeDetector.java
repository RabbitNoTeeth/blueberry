package fun.bookish.blueberry.opencv.detector;


import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Scalar;

/**
 * 条纹检测器
 */
public class StripeDetector {

    private float threshold = 0.008f;

    public StripeDetector() {
    }

    public StripeDetector(float threshold) {
        this.threshold = threshold;
    }

    private Result doDetect(Mat image) {
        Mat hsv = new Mat();
        opencv_imgproc.cvtColor(image, hsv, opencv_imgproc.COLOR_BGR2HSV);
        MatVector hsvChannels = new MatVector();
        opencv_core.split(hsv, hsvChannels);
        Mat channelH = hsvChannels.get(0);
        int m = opencv_core.getOptimalDFTSize(channelH.rows());
        int n = opencv_core.getOptimalDFTSize(channelH.cols());
        opencv_core.copyMakeBorder(channelH, channelH, 0, m - channelH.rows(), 0, n - channelH.cols(), opencv_core.BORDER_CONSTANT, new Scalar(0));
        Mat mFourier = new Mat(channelH.rows() + m, channelH.cols() + n, opencv_core.CV_32FC2, new Scalar(0, 0));
        MatVector mForFourier = new MatVector();
        Mat m1 = new Mat();
        channelH.convertTo(m1, opencv_core.CV_32F);
        mForFourier.push_back(m1);
        mForFourier.push_back(Mat.zeros(channelH.size(), opencv_core.CV_32F).asMat());
        Mat mSrc = Mat.zeros(channelH.size(), opencv_core.CV_32F).asMat();
        opencv_core.merge(mForFourier, mSrc);
        opencv_core.dft(mSrc, mFourier);
        MatVector channels = new MatVector();
        opencv_core.split(mFourier, channels);
        Mat mRe = channels.get(0);
        Mat mIm = channels.get(1);
        Mat mAmplitude = new Mat();
        opencv_core.magnitude(mRe, mIm, mAmplitude);
        opencv_core.add(mAmplitude, mAmplitude);
        opencv_core.log(mAmplitude, mAmplitude);
        Mat means = new Mat();
        Mat stddev = new Mat();
        opencv_core.meanStdDev(mAmplitude, means, stddev);
        DoubleRawIndexer meansIndexer = means.createIndexer();
        DoubleRawIndexer stddevIndexer = stddev.createIndexer();
        double men = meansIndexer.get(0);
        double std = stddevIndexer.get(0);
        double[] min = new double[1];
        double[] max = new double[1];
        opencv_core.minMaxLoc(mAmplitude, min, max, null, null, null);
        double max_v = max[0];
        double min_v = min[0];
        double T = Math.max(men + 3 * std, max_v / 2);
        double count = 0;
        int height = mAmplitude.rows();
        int width = mAmplitude.cols();
        FloatRawIndexer mAmplitudeIndexer = mAmplitude.createIndexer();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mAmplitudeIndexer.get(i, j) > T) {
                    count++;
                }
            }
        }
        double res = count / (height * width);
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
