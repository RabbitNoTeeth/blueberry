package fun.bookish.blueberry.core.aop;

import fun.bookish.blueberry.core.exception.ParameterValidateException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 参数校验拓展aop，用于校验基本数据类型
 * @author LiuXinDong
 */
@Component
@Aspect
@Order(1)
public class ParameterValidateExtAop {

    private final ExecutableValidator validator = Validation.buildDefaultValidatorFactory().getValidator().forExecutables();

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void intercept(){

    }

    @Before("intercept()")
    public void beforeInvoke(JoinPoint joinPoint) throws BindException {
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Set<ConstraintViolation<Object>> violationSet = validator.validateParameters(target, method, joinPoint.getArgs());
        if(violationSet.size() > 0){
            String message = violationSet.stream().map(ConstraintViolation::getMessage).reduce("", (x, y) -> x + y + "; ");
            // 抛出校验失败异常，该异常会在 GlobalExceptionAdvice 中统一被处理
            throw new ParameterValidateException(message);
        }
    }

}
