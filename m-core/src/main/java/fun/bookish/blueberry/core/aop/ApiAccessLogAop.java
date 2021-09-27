package fun.bookish.blueberry.core.aop;


import fun.bookish.blueberry.core.utils.HttpUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 接口请求日志aop
 *
 * @author LIUXINDONG
 */
@Aspect
@Component
@Order(2)
public class ApiAccessLogAop {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ThreadLocal<Map<String, Object>> requestInfoCache = new ThreadLocal<>();

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void intercept() {

    }

    @Before("intercept()")
    public void beforeExecute(JoinPoint joinPoint) throws Exception {
        if (!HttpUtils.hasContext()) {
            return;
        }
        HttpServletRequest request = HttpUtils.getCurrentHttpServletRequest();
        String requestUrl = HttpUtils.getRequestUrl();
        String requestMethod = HttpUtils.getRequestMethod();
        requestUrl = requestMethod.toUpperCase() + ":" + requestUrl;
        String timestamp = System.nanoTime() + "";
        Map<String, String[]> params = request.getParameterMap();
        LOGGER.info(">> 接收Http请求[{}]: {}, params: {}", timestamp, requestUrl, JsonUtils.encode(params));
        Map<String, Object> requestInfoCacheMap = new HashMap<>();
        requestInfoCacheMap.put("requestUrl", requestUrl);
        requestInfoCacheMap.put("timestamp", timestamp);
        // 记录起始时间
        long startTime = System.currentTimeMillis();
        requestInfoCacheMap.put("startTime", startTime);
        requestInfoCache.set(requestInfoCacheMap);
    }

    @AfterReturning("intercept()")
    public void afterReturning(JoinPoint joinPoint) {
        if (!HttpUtils.hasContext()) {
            return;
        }
        Map<String, Object> requestInfo = requestInfoCache.get();
        if (requestInfo == null) {
            return;
        }
        String requestUrl = requestInfo.get("requestUrl").toString();
        String timestamp = requestInfo.get("timestamp").toString();
        long startTime = Long.parseLong(requestInfo.get("startTime").toString());
        // 记录结束时间
        long endTime = System.currentTimeMillis();
        // 计算执行时间
        long costTime = endTime - startTime;
        LOGGER.info("<< 响应Http请求[{}], cost: {}ms", timestamp, costTime);
    }

    @AfterThrowing(value = "intercept()", throwing = "e")
    public Object throwException(JoinPoint joinPoint, Throwable e) {
        if (!HttpUtils.hasContext()) {
            return e;
        }
        Map<String, Object> requestInfo = requestInfoCache.get();
        if (requestInfo == null) {
            return e;
        }
        String requestUrl = requestInfo.get("requestUrl").toString();
        String timestamp = requestInfo.get("timestamp").toString();
        LOGGER.error("(x) Http请求[{}]异常: {}", timestamp, requestUrl, e);
        return e;
    }

    @Around("intercept()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 执行
        Object result = pjp.proceed();
        return result;
    }

}
