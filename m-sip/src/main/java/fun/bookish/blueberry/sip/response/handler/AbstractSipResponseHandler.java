package fun.bookish.blueberry.sip.response.handler;

import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sip响应处理器接口
 */
public abstract class AbstractSipResponseHandler implements SipResponseHandler {

    private static final Logger LOGGER_ = LoggerFactory.getLogger(AbstractSipResponseHandler.class);

    /**
     * 进行响应处理
     *
     * @param responseWrapper
     */
    public final void handle(SipResponseWrapper responseWrapper){
        beforeHandle(responseWrapper);
        doHandle(responseWrapper);
        afterHandle(responseWrapper);
    }

}
