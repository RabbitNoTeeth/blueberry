package fun.bookish.blueberry.sip.utils;

/**
 * SN序列号工具类
 */
public class SipSNUtils {

    private SipSNUtils() {}

    /**
     * 生成SN
     * @return
     */
    public static int generateSN() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

}
