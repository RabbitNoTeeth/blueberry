package fun.bookish.blueberry.core.advice;

import fun.bookish.blueberry.core.annotation.DisableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.annotation.EnableResponseBodyJsonWrap;
import fun.bookish.blueberry.core.json.JsonResult;
import fun.bookish.blueberry.core.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ControllerResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getMethodAnnotation(DisableResponseBodyJsonWrap.class) != null) {
            return false;
        }
        return methodParameter.getMethodAnnotation(EnableResponseBodyJsonWrap.class) != null || methodParameter.getContainingClass().getAnnotation(EnableResponseBodyJsonWrap.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return JsonResult.success("请求成功", o);
    }
}
