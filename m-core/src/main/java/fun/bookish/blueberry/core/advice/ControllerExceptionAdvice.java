package fun.bookish.blueberry.core.advice;

import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.exception.ParameterValidateException;
import fun.bookish.blueberry.core.json.JsonResult;
import fun.bookish.blueberry.core.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理器
 * @author LIUXINDONG
 */
@ControllerAdvice
public class ControllerExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    /**
     * 拦截参数校验异常
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler(BindException.class)
    public void handleBindException(HttpServletResponse response, BindException e) throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).reduce("", (x, y) -> x + y + ";");
        // 写出响应
        writeResponse(response, JsonResult.fail(message));
    }

    /**
     * 拦截参数校验异常
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler(ParameterValidateException.class)
    public void handleParameterValidateException(HttpServletResponse response, ParameterValidateException e) throws IOException {
        // 写出响应
        writeResponse(response, JsonResult.fail(e.getMessage()));
    }

    /**
     * 拦截开发者主动抛出的业务异常
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler(ManualRollbackException.class)
    public void handleSpecificOperatingException(HttpServletResponse response, ManualRollbackException e) throws IOException {
        LOGGER.error("", e);
        // 写出响应
        writeResponse(response, JsonResult.fail(e.getMessage()));
    }

    /**
     * 拦截其他未知的异常
     * @param request
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        LOGGER.error("Controller接口异常", e);
        // 写出响应
        response.setStatus(500);
        writeResponse(response, "程序发生未知错误，我们已记录该错误并会在第一时间修复。对您造成的不便敬请谅解！");
    }

    /**
     * 写出响应
     * @param response
     * @param message
     * @throws IOException
     */
    private void writeResponse(HttpServletResponse response, JsonResult message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JsonUtils.encode(message));
    }

    /**
     * 写出响应
     * @param response
     * @param message
     * @throws IOException
     */
    private void writeResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JsonUtils.encode(message));
    }

}
