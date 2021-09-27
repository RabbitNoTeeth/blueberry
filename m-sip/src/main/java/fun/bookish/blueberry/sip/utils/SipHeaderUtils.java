package fun.bookish.blueberry.sip.utils;

import fun.bookish.blueberry.sip.digest.MessageDigestTypeEnum;

import javax.sip.SipFactory;
import javax.sip.header.WWWAuthenticateHeader;
import java.util.Date;
import java.util.Random;

/**
 * sip header工具类
 */
public class SipHeaderUtils {

    private SipHeaderUtils() {
    }

    public static WWWAuthenticateHeader createWWWAuthenticateHeader(SipFactory sipFactory, MessageDigestTypeEnum digestTypeEnum, String realm) {
        try {
            WWWAuthenticateHeader header = sipFactory.createHeaderFactory().createWWWAuthenticateHeader("Digest");
            header.setParameter("realm", realm);
            header.setParameter("nonce", generateNonce(digestTypeEnum));
            header.setParameter("opaque", "");
            header.setParameter("stale", "FALSE");
            header.setParameter("algorithm", digestTypeEnum.getDesc());
            return header;
        } catch (Exception e) {
            throw new RuntimeException("(×) Failed to create a WWWAuthenticateHeader", e);
        }
    }

    /**
     * 生成nonce
     *
     * @param digestTypeEnum
     * @return
     */
    private static String generateNonce(MessageDigestTypeEnum digestTypeEnum) {
        // Get the time of day and run MD5 over it.
        Date date = new Date();
        long time = date.getTime();
        Random rand = new Random();
        long pad = rand.nextLong();
        String nonceString = (new Long(time)).toString()
                + (new Long(pad)).toString();
        byte mdbytes[] = MessageDigestUtils.digest(digestTypeEnum, nonceString.getBytes());
        // Convert the mdbytes array into a hex string.
        return MessageDigestUtils.toHexString(mdbytes);
    }

}
