package fun.bookish.blueberry.sip.constant;

/**
 * resultType枚举
 */
public enum SipResultTypeEnum {

    OK("OK"),
    ERROR("ERROR");

    private final String code;

    SipResultTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
