package fun.bookish.blueberry.sip.command.executor;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.exception.SipClientTransactionCreationException;

import javax.sip.ClientTransaction;
import javax.sip.SipProvider;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

/**
 * sip命令执行器接口
 */
public interface SipCommandExecutor {

    /**
     * 获取会话事务
     * @param protocolType
     * @param request
     * @return
     * @throws SipClientTransactionCreationException
     */
    default ClientTransaction getClientTransaction(String protocolType, Request request) throws SipClientTransactionCreationException {
        try {
            SipServer sipServer = getSipServer();
            SipProvider tcpSipProvider = sipServer.getTcpSipProvider();
            SipProvider udpSipProvider = sipServer.getUdpSipProvider();
            return "TCP".equals(protocolType) ? tcpSipProvider.getNewClientTransaction(request) : udpSipProvider.getNewClientTransaction(request);
        } catch (Exception e) {
            throw new SipClientTransactionCreationException("(×) Failed to get client transaction", e);
        }
    }

    SipServer getSipServer();

}
