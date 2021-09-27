package fun.bookish.blueberry.sip.constant;

/**
 * online枚举
 */
public enum SipOnlineEnum {

    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");

    private final String code;

    SipOnlineEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
