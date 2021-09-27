package fun.bookish.blueberry.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * 网络工具类
 */
public class NetUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);
    private final static Pattern PING_SUCCESS_PATTERN = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);

    private NetUtils() {
    }

    /**
     * ping
     *
     * @param ip      目标ip地址
     * @param timeout 超时时长
     * @return 能否ping通
     * @throws Exception
     */
    public static boolean ping(String ip, int timeout) throws Exception {
        return InetAddress.getByName(ip).isReachable(timeout);
    }

    /**
     * ping
     *
     * @param ip        目标ip地址
     * @param pingTimes ping次数
     * @param timeout   超时时长
     * @return 丢包率
     */
    public static PingResult ping(String ip, int pingTimes, int timeout) throws PingException {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();   //  将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = SystemUtils.isWin() ? ("ping " + ip + " -n " + pingTimes + " -w " + timeout) : ("ping " + ip + " -c " + pingTimes);
        try {
            Process p = r.exec(pingCommand);
            if (p == null) {
                throw new PingException("ping命令执行失败");
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int packetLossCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                //  逐行检查输出,如果出现类似=23ms TTL=62的字样，那么说明ping通
                if (!PING_SUCCESS_PATTERN.matcher(line).matches()) {
                    packetLossCount++;
                }
            }
            return new PingResult(packetLossCount, packetLossCount * 100 / pingTimes);
        } catch (Exception e) {
            throw new PingException("ping命令执行异常", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.error("BufferedReader关闭异常", e);
            }
        }
    }

    public static class PingResult {
        // 丢包次数
        private final int packetLossCount;
        // 丢包率
        private final int packetLossRate;

        public PingResult(int packetLossCount, int packetLossRate) {
            this.packetLossCount = packetLossCount;
            this.packetLossRate = packetLossRate;
        }

        public int getPacketLossCount() {
            return packetLossCount;
        }

        public int getPacketLossRate() {
            return packetLossRate;
        }

    }

    public static class PingException extends Exception {
        public PingException(String message) {
            super(message);
        }

        public PingException(String message, Throwable e) {
            super(message, e);
        }
    }

}
