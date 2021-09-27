package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipRequestCreationException extends Exception {

    public SipRequestCreationException() {

    }

    public SipRequestCreationException(String message) {
        super(message);
    }

    public SipRequestCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipRequestCreationException(Throwable e) {
        super(e);
    }

}
