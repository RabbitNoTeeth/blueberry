package fun.bookish.blueberry.core.exception;

/**
 * 参数校验失败异常
 * @author LIUXINDONG
 */
public class ParameterValidateException extends RuntimeException {
    private static final long serialVersionUID = -2453286880148054962L;

    public ParameterValidateException(String message){
        super(message);
    }

}
