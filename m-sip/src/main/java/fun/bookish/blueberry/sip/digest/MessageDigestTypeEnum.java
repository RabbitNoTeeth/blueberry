package fun.bookish.blueberry.sip.digest;

/**
 * 身份认证中数字加密方式
 */
public enum MessageDigestTypeEnum {

    MD5("MD5");

    private final String desc;

    MessageDigestTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
