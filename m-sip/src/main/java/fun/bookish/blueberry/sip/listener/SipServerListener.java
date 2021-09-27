package fun.bookish.blueberry.sip.listener;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.handler.SipHandlerFactory;
import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import fun.bookish.blueberry.sip.utils.SipResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.*;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Response;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * sip服务默认监听器
 */
public class SipServerListener implements SipListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipServerListener.class);

    private final SipServer sipServer;

    private final ExecutorService executorService;

    public SipServerListener(SipServer sipServer) {
        this.sipServer = sipServer;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        // 请求包装预处理
        SipRequestWrapper requestWrapper = wrapRequest(requestEvent);
        this.executorService.execute(() -> {
            String method = requestEvent.getRequest().getMethod();
            SipHandlerFactory.getRequestHandler(method).handle(requestWrapper);
        });
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        // 响应包装预处理
        SipResponseWrapper responseWrapper = wrapResponse(responseEvent);
        this.executorService.execute(() -> {
            Response response = responseEvent.getResponse();
            String responseDesc = SipResponseUtils.getSimpleDesc(response);
            CSeqHeader cseqHeader = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
            String method = cseqHeader.getMethod();
            SipHandlerFactory.getResponseHandler(method).handle(responseWrapper);
//            int statusCode = response.getStatusCode();
//            if (statusCode >= Response.BAD_REQUEST) {
//                String reasonPhrase = response.getReasonPhrase();
//                LOGGER.warn("(!) >>> Receive an abnormal '{}' response with status '{}', the reason phrases to bo '{}'. From:{}", method, statusCode, reasonPhrase, responseDesc);
//            } else if (statusCode >= Response.MULTIPLE_CHOICES) {
//                // todo 完善300+状态码响应的处理
//            } else if (statusCode >= Response.OK) {
//                SipHandlerFactory.getResponseHandler(method).handle(responseWrapper);
//            } else {
//                // todo 完善100+状态码响应的处理
//            }
        });
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {

    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

    }

    /**
     * 请求包装预处理
     *
     * @param requestEvent
     * @return
     */
    private SipRequestWrapper wrapRequest(RequestEvent requestEvent) {
        return new SipRequestWrapper(this.sipServer, requestEvent);
    }

    /**
     * 响应包装预处理
     * @param responseEvent
     * @return
     */
    private SipResponseWrapper wrapResponse(ResponseEvent responseEvent) {
        return new SipResponseWrapper(this.sipServer, responseEvent);
    }

}
