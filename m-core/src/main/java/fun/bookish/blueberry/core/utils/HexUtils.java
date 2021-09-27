package fun.bookish.blueberry.core.utils;


public class HexUtils {

    private HexUtils() {}

    /**
     * int数字转十六进制
     * @param n
     * @return
     */
    public static String int2Hex(int n) {
        String res = Integer.toHexString(n);
        if (res.length() % 2 == 1) {
            res = "0" + res;
        }
        return res.toUpperCase();
    }

    /**
     * 十六进制转int数字
     * @param hex
     * @return
     */
    public static int hex2Int(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * long数字转十六进制
     * @param n
     * @return
     */
    public static String long2Hex(long n) {
        String res = Long.toHexString(n);
        if (res.length() % 2 == 1) {
            res = "0" + res;
        }
        return res.toUpperCase();
    }

    /**
     * 十六进制转long数字
     * @param hex
     * @return
     */
    public static long hex2Long(String hex) {
        return Long.parseLong(hex, 16);
    }

    /**
     * double数字转十六进制
     * @param n
     * @return
     */
    public static String double2Hex(double n) {
        String res = Double.toHexString(n);
        if (res.length() % 2 == 1) {
            res = "0" + res;
        }
        return res.toUpperCase();
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHex(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return str.toString().toUpperCase();
    }

}
