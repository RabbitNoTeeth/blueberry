package fun.bookish.blueberry.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    private Base64Utils() {}

    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode2bytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String decode2string(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

}
