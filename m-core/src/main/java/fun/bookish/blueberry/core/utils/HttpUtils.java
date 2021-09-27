package fun.bookish.blueberry.core.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * http工具类
 *
 * @author LIUXINDONG
 */
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 检查线程是否具有http请求上下文
     *
     * @return
     */
    public static boolean hasContext() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 共享线程上下文
     */
    public static void shareContext() {
        ServletRequestAttributes sra = getServletRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
    }

    /**
     * 获取当前ServletRequestAttributes
     *
     * @return
     */
    private static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 是否有效
     *
     * @return
     */
    public static boolean valid() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 获取当前HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getCurrentHttpServletRequest() {
        if (!valid()) {
            return null;
        }
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取当前HttpServletResponse
     *
     * @return
     */
    public static HttpServletResponse getCurrentHttpServletResponse() {
        if (!valid()) {
            return null;
        }
        return getServletRequestAttributes().getResponse();
    }

    /**
     * 获取所有请求头
     * @return
     */
    public static Map<String, String> getHeaders() {
        if (!valid()) {
            return null;
        }
        Map<String, String> result = new LinkedHashMap<>();
        HttpServletRequest request = getCurrentHttpServletRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            result.put(headerName, request.getHeader(headerName));
        }
        return result;
    }

    /**
     * 获取请求接口路径
     *
     * @return
     */
    public static String getRequestUrl() {
        if (!valid()) {
            return null;
        }
        return getCurrentHttpServletRequest().getServletPath();
    }

    /**
     * 获取接口请求方法
     *
     * @return
     */
    public static String getRequestMethod() {
        if (!valid()) {
            return null;
        }
        return getCurrentHttpServletRequest().getMethod();
    }

    /**
     * 获取请求方ip地址
     *
     * @return
     */
    public static String getRemoteAddr() {
        if (!valid()) {
            return null;
        }
        return getCurrentHttpServletRequest().getRemoteAddr();
    }

    /**
     * 获取请求头
     *
     * @return
     */
    public static String getRequestHeader(String name) {
        if (!valid()) {
            return null;
        }
        return getCurrentHttpServletRequest().getHeader(name);
    }

}
