package fun.bookish.blueberry.sip.exception;

/**
 * sip客户端会话事务创建异常
 */
public class SipClientTransactionCreationException extends Exception {

    public SipClientTransactionCreationException() {

    }

    public SipClientTransactionCreationException(String message) {
        super(message);
    }

    public SipClientTransactionCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipClientTransactionCreationException(Throwable e) {
        super(e);
    }

}
