package fun.bookish.blueberry.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件工具类
 */
public class FileUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * 保存文件
     *
     * @param path      文件目录路径
     * @param fileName  文件名称
     * @param fileBytes 文件数据
     * @throws FileSaveException
     */
    public static void save(String path, String fileName, byte[] fileBytes) throws FileSaveException {
        OutputStream out = null;
        try {
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            out = new FileOutputStream(filePath.getPath() + "/" + fileName);
            out.write(fileBytes);
        } catch (Exception e) {
            throw new FileSaveException("文件保存失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("文件输出流关闭异常", e);
                }
            }
        }
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     */
    public static byte[] read(String filePath) throws FileReadException {
        InputStream in = null;
        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            int length = in.available();
            byte[] res = new byte[length];
            in.read(res);
            return res;
        } catch (Exception e) {
            throw new FileReadException("文件读取失败", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("文件输入流关闭异常", e);
                }
            }
        }
    }

    public static class FileSaveException extends Exception {
        public FileSaveException(String message) {
            super(message);
        }

        public FileSaveException(String message, Throwable e) {
            super(message, e);
        }
    }

    public static class FileReadException extends Exception {
        public FileReadException(String message) {
            super(message);
        }

        public FileReadException(String message, Throwable e) {
            super(message, e);
        }
    }

}
