package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipDeviceStatusResponseCreationException extends Exception {

    public SipDeviceStatusResponseCreationException() {

    }

    public SipDeviceStatusResponseCreationException(String message) {
        super(message);
    }

    public SipDeviceStatusResponseCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipDeviceStatusResponseCreationException(Throwable e) {
        super(e);
    }

}
