package fun.bookish.blueberry.sip.command.executor.query;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.exception.SipClientTransactionCreationException;

import javax.sip.ClientTransaction;
import javax.sip.SipProvider;
import javax.sip.message.Request;

public abstract class AbstractSipQueryCommandExecutor implements SipQueryCommandExecutor{

    protected final SipServer sipServer;

    protected AbstractSipQueryCommandExecutor(SipServer sipServer) {
        this.sipServer = sipServer;
    }

    public final SipServer getSipServer() {
        return this.sipServer;
    }

}
