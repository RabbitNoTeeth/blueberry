package fun.bookish.blueberry.opencv.utils;


import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {

    public static void show(String imagePath, String text) {
        Mat image = opencv_imgcodecs.imread(imagePath);
        opencv_imgproc.putText(image, text, new Point(5, 50), 0, 0.5, new Scalar(0, 255));
        opencv_highgui.imshow(imagePath, image);
        opencv_highgui.waitKey(0);
        opencv_highgui.destroyWindow(imagePath);
    }

    public static List<String> getImageList(String path) {
        List<String> res = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null)
                Arrays.stream(subFiles).forEach(subFile -> {
                    String name = subFile.getName();
                    if (name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")) {
                        res.add(subFile.getAbsolutePath());
                    }
                });
        }
        return res;
    }

}
