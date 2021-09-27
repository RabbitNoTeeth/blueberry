package fun.bookish.blueberry.opencv.detector;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

/**
 * 差异检测器
 */
public class DifferenceDetector {

    private final Arithmetic DEFAULT_ARITHMETIC = Arithmetic.AVERAGE_HASH;

    private int threshold = 30;

    public DifferenceDetector() {
    }

    public DifferenceDetector(int threshold) {
        this.threshold = threshold;
    }

    /**
     * 平均哈希算法
     *
     * @param image1
     * @param image2
     * @return
     */
    private int averageHash(Mat image1, Mat image2) {
        Mat matDst1 = new Mat();
        Mat matDst2 = new Mat();
        opencv_imgproc.resize(image1, matDst1, new Size().width(8).height(8), 0, 0, opencv_imgproc.INTER_CUBIC);
        opencv_imgproc.resize(image2, matDst2, new Size().width(8).height(8), 0, 0, opencv_imgproc.INTER_CUBIC);

        opencv_imgproc.cvtColor(matDst1, matDst1, opencv_imgproc.CV_BGR2GRAY);
        opencv_imgproc.cvtColor(matDst2, matDst2, opencv_imgproc.CV_BGR2GRAY);

        int iAvg1 = 0, iAvg2 = 0;
        int[] arr1 = new int[64];
        int[] arr2 = new int[64];

        for (int i = 0; i < 8; i++) {
            BytePointer data1 = matDst1.ptr(i);
            BytePointer data2 = matDst2.ptr(i);
            int tmp = i * 8;
            for (int j = 0; j < 8; j++) {
                int tmp1 = tmp + j;
                arr1[tmp1] = (data1.get(j) & 0xff) / 4 * 4;
                arr2[tmp1] = (data2.get(j) & 0xff) / 4 * 4;
                iAvg1 += arr1[tmp1];
                iAvg2 += arr2[tmp1];
            }
        }

        iAvg1 /= 64;
        iAvg2 /= 64;

        for (int i = 0; i < 64; i++) {
            arr1[i] = (arr1[i] >= iAvg1) ? 1 : 0;
            arr2[i] = (arr2[i] >= iAvg2) ? 1 : 0;
        }

        int iDiffNum = 0;

        for (int i = 0; i < 64; i++)
            if (arr1[i] != arr2[i])
                ++iDiffNum;

        return iDiffNum;
    }

    /**
     * 感知哈希算法
     *
     * @param image1
     * @param image2
     * @return
     */
    private int perceptualHash(Mat image1, Mat image2) {
        Mat matDst1 = new Mat();
        Mat matDst2 = new Mat();
        opencv_imgproc.resize(image1, matDst1, new Size().width(32).height(32), 0, 0, opencv_imgproc.INTER_CUBIC);
        opencv_imgproc.resize(image2, matDst2, new Size().width(32).height(32), 0, 0, opencv_imgproc.INTER_CUBIC);

        opencv_imgproc.cvtColor(matDst1, matDst1, opencv_imgproc.CV_BGR2GRAY);
        opencv_imgproc.cvtColor(matDst2, matDst2, opencv_imgproc.CV_BGR2GRAY);

        matDst1.convertTo(matDst1, opencv_core.CV_32F);
        matDst2.convertTo(matDst2, opencv_core.CV_32F);
        opencv_core.dct(matDst1, matDst1);
        opencv_core.dct(matDst2, matDst2);

        int iAvg1 = 0, iAvg2 = 0;
        int[] arr1 = new int[64];
        int[] arr2 = new int[64];

        for (int i = 0; i < 8; i++) {
            BytePointer data1 = matDst1.ptr(i);
            BytePointer data2 = matDst2.ptr(i);
            int tmp = i * 8;
            for (int j = 0; j < 8; j++) {
                int tmp1 = tmp + j;
                arr1[tmp1] = data1.get(j) & 0xff;
                arr2[tmp1] = data2.get(j) & 0xff;
                iAvg1 += arr1[tmp1];
                iAvg2 += arr2[tmp1];
            }
        }

        iAvg1 /= 64;
        iAvg2 /= 64;

        for (int i = 0; i < 64; i++) {
            arr1[i] = (arr1[i] >= iAvg1) ? 1 : 0;
            arr2[i] = (arr2[i] >= iAvg2) ? 1 : 0;
        }

        int iDiffNum = 0;

        for (int i = 0; i < 64; i++)
            if (arr1[i] != arr2[i])
                ++iDiffNum;

        return iDiffNum;
    }

    private Result doDetect(Mat image1, Mat image2, Arithmetic arithmetic) {
        int difference = 100;
        switch (arithmetic) {
            case AVERAGE_HASH:
                difference = averageHash(image1, image2);
                break;
            case PERCEPTUAL_HASH:
                difference = perceptualHash(image1, image2);
                break;
            default:
                break;
        }
        return new Result(threshold, difference, difference > threshold);
    }

    public Result detect(Mat image1, Mat image2) {
        return doDetect(image1, image2, DEFAULT_ARITHMETIC);
    }

    public Result detect(Mat image1, Mat image2, Arithmetic arithmetic) {
        return doDetect(image1, image2, arithmetic);
    }

    public Result detect(String imagePath1, String imagePath2) {
        Mat image1 = opencv_imgcodecs.imread(imagePath1);
        Mat image2 = opencv_imgcodecs.imread(imagePath2);
        return doDetect(image1, image2, DEFAULT_ARITHMETIC);
    }

    public Result detect(String imagePath1, String imagePath2, Arithmetic arithmetic) {
        Mat image1 = opencv_imgcodecs.imread(imagePath1);
        Mat image2 = opencv_imgcodecs.imread(imagePath2);
        return doDetect(image1, image2, arithmetic);
    }

    public static enum Arithmetic {
        AVERAGE_HASH,
        PERCEPTUAL_HASH
    }

    public static class Result {
        private final int threshold;
        private final int result;
        private final boolean hasError;

        Result(int threshold, int result, boolean hasError) {
            this.threshold = threshold;
            this.result = result;
            this.hasError = hasError;
        }

        public boolean isHasError() {
            return hasError;
        }

        public int getThreshold() {
            return threshold;
        }

        public int getResult() {
            return result;
        }
    }

}
