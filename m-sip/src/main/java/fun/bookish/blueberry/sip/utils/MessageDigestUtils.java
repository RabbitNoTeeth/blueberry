package fun.bookish.blueberry.sip.utils;

import fun.bookish.blueberry.sip.digest.MessageDigestTypeEnum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数字摘要工具类
 */
public class MessageDigestUtils {

    private static final Map<MessageDigestTypeEnum, MessageDigest> MESSAGEDIGEST_MAP = new HashMap<>();

    static {
        try {
            for (MessageDigestTypeEnum typeEnum : MessageDigestTypeEnum.values()) {
                MESSAGEDIGEST_MAP.put(typeEnum, MessageDigest.getInstance(typeEnum.getDesc()));
            }
        } catch (Exception e) {
            throw new RuntimeException("数字摘要工具类初始化失败", e);
        }
    }

    /** to hex converter */
    private static final char[] toHex = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


    public static MessageDigest getMessageDigest(MessageDigestTypeEnum typeEnum) {
        return MESSAGEDIGEST_MAP.get(typeEnum);
    }

    public static String toHexString(byte b[]) {
        int pos = 0;
        char[] c = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            c[pos++] = toHex[(b[i] >> 4) & 0x0F];
            c[pos++] = toHex[b[i] & 0x0f];
        }
        return new String(c);
    }


    public static byte[] digest(MessageDigestTypeEnum digestTypeEnum, byte[] bytes) {
        return getMessageDigest(digestTypeEnum).digest(bytes);
    }
}
