package fun.bookish.blueberry.sip.exception;

/**
 * sip请求创建异常
 */
public class SipCatalogResponseCreationException extends Exception {

    public SipCatalogResponseCreationException() {

    }

    public SipCatalogResponseCreationException(String message) {
        super(message);
    }

    public SipCatalogResponseCreationException(String message, Throwable e) {
        super(message, e);
    }

    public SipCatalogResponseCreationException(Throwable e) {
        super(e);
    }

}
