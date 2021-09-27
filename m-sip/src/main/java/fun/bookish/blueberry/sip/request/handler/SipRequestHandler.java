package fun.bookish.blueberry.sip.request.handler;

import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.*;

/**
 * sip请求处理器接口
 */
public interface SipRequestHandler {

    /**
     * 请求预处理
     * @param requestWrapper
     */
    void beforeHandle(SipRequestWrapper requestWrapper) throws Exception;

    /**
     * 进行请求处理
     *
     * @param requestWrapper
     */
    void handle(SipRequestWrapper requestWrapper);

    /**
     * 处理请求
     * @param requestWrapper
     */
    void doHandle(SipRequestWrapper requestWrapper) throws Exception;

    /**
     * 请求后置处理
     * @param requestWrapper
     */
    void afterHandle(SipRequestWrapper requestWrapper) throws Exception;

    /**
     * 获取请求的会话事务
     *
     * @param requestWrapper
     * @return
     */
    ServerTransaction getServerTransaction(SipRequestWrapper requestWrapper);

}
