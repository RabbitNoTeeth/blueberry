package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipDeviceInfoResponseCreationException extends Exception {

    public SipDeviceInfoResponseCreationException() {

    }

    public SipDeviceInfoResponseCreationException(String message) {
        super(message);
    }

    public SipDeviceInfoResponseCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipDeviceInfoResponseCreationException(Throwable e) {
        super(e);
    }

}
