package fun.bookish.blueberry.sip.constant;

/**
 * statusType枚举
 */
public enum SipStatusTypeEnum {

    ON("ON"),
    OFF("OFF");

    private final String code;

    SipStatusTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
