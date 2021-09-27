package fun.bookish.blueberry.sip.utils;

public class SipTagUtils {

    private SipTagUtils() {}

    /**
     * 生成SN
     * @return
     */
    public static String random() {
        return System.nanoTime() + "";
    }

}
