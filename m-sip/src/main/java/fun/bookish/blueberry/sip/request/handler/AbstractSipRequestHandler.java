package fun.bookish.blueberry.sip.request.handler;

import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.utils.SipRequestUtils;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.*;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

/**
 * sip请求处理器接口
 */
public abstract class AbstractSipRequestHandler implements SipRequestHandler{

    private static final Logger LOGGER_ = LoggerFactory.getLogger(AbstractSipRequestHandler.class);

    /**
     * 进行请求处理
     *
     * @param requestWrapper
     */
    public final void handle(SipRequestWrapper requestWrapper){
        try {
            beforeHandle(requestWrapper);
            doHandle(requestWrapper);
            afterHandle(requestWrapper);
        } catch (Exception e) {
            Request request = requestWrapper.getRequestEvent().getRequest();
            String requestDesc = SipRequestUtils.getSimpleDesc(request);
            String method = request.getMethod();
            LOGGER_.error("(×) Failed to handle a {} request. From:{}", method, requestDesc, e);
        }
    }

    /**
     * 获取请求的会话事务
     *
     * @param requestWrapper
     * @return
     */
    public final ServerTransaction getServerTransaction(SipRequestWrapper requestWrapper) {
        RequestEvent requestEvent = requestWrapper.getRequestEvent();
        SipProvider tcpSipProvider = requestWrapper.getSipServer().getTcpSipProvider();
        SipProvider udpSipProvider = requestWrapper.getSipServer().getUdpSipProvider();
        Request request = requestEvent.getRequest();
        ServerTransaction serverTransaction = requestEvent.getServerTransaction();
        // 判断TCP还是UDP
        ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        String transport = reqViaHeader.getTransport();
        boolean isTcp = transport.equals("TCP");
        if (serverTransaction == null) {
            try {
                if (isTcp) {
                    SipStackImpl stack = (SipStackImpl) tcpSipProvider.getSipStack();
                    serverTransaction = (SIPServerTransaction) stack.findTransaction((SIPRequest) request, true);
                    if (serverTransaction == null) {
                        serverTransaction = tcpSipProvider.getNewServerTransaction(request);
                    }
                } else {
                    SipStackImpl stack = (SipStackImpl) udpSipProvider.getSipStack();
                    serverTransaction = (SIPServerTransaction) stack.findTransaction((SIPRequest) request, true);
                    if (serverTransaction == null) {
                        serverTransaction = udpSipProvider.getNewServerTransaction(request);
                    }
                }
            } catch (TransactionAlreadyExistsException | TransactionUnavailableException e) {
                if (e instanceof TransactionAlreadyExistsException) {
                    SipStackImpl stack = isTcp ? (SipStackImpl) tcpSipProvider.getSipStack() : (SipStackImpl) udpSipProvider.getSipStack();
                    return (SIPServerTransaction) stack.findTransaction((SIPRequest) request, true);
                } else {
                    LOGGER_.error("(×) Failed to get server transaction", e);
                }
            }
        }
        return serverTransaction;
    }

}
