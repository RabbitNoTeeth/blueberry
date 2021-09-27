package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipKeepaliveResponseCreationException extends Exception {

    public SipKeepaliveResponseCreationException() {

    }

    public SipKeepaliveResponseCreationException(String message) {
        super(message);
    }

    public SipKeepaliveResponseCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipKeepaliveResponseCreationException(Throwable e) {
        super(e);
    }

}
