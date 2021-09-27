package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipCommandExecuteException extends Exception {

    public SipCommandExecuteException() {

    }

    public SipCommandExecuteException(String message) {
        super(message);
    }

    public SipCommandExecuteException(String message, Throwable e) {
        super(message, e);
    }

    public SipCommandExecuteException(Throwable e) {
        super(e);
    }

}
