package fun.bookish.blueberry.sip.response.handler;

import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;

/**
 * sip请求处理器接口
 */
public interface SipResponseHandler {

    /**
     * 请求预处理
     * @param responseWrapper
     */
    void beforeHandle(SipResponseWrapper responseWrapper);

    /**
     * 进行请求处理
     *
     * @param responseWrapper
     */
    void handle(SipResponseWrapper responseWrapper);

    /**
     * 处理请求
     * @param responseWrapper
     */
    void doHandle(SipResponseWrapper responseWrapper);

    /**
     * 请求后置处理
     * @param responseWrapper
     */
    void afterHandle(SipResponseWrapper responseWrapper);

}
