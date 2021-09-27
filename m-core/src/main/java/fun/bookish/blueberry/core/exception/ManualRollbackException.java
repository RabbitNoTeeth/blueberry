package fun.bookish.blueberry.core.exception;

/**
 * 手动回滚事务异常类，用于手动回滚事务
 * @author LIUXINDONG
 */
public class ManualRollbackException extends RuntimeException{
    private static final long serialVersionUID = 1294595729299207352L;

    public ManualRollbackException(String message) {
        super(message);
    }

    public ManualRollbackException(String message, Throwable e) {
        super(message, e);
    }

}
