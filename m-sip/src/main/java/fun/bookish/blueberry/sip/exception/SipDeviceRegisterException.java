package fun.bookish.blueberry.sip.exception;

/**
 * 设备注册异常
 */
public class SipDeviceRegisterException extends Exception {

    public SipDeviceRegisterException() {

    }

    public SipDeviceRegisterException(String message) {
        super(message);
    }

    public SipDeviceRegisterException(String message, Throwable e) {
        super(message, e);
    }

    public SipDeviceRegisterException(Throwable e) {
        super(e);
    }

}
