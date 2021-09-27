package fun.bookish.blueberry.sip.command.executor.media;

import fun.bookish.blueberry.sip.SipServer;

public abstract class AbstractSipMediaCommandExecutor implements SipMediaCommandExecutor {

    protected final SipServer sipServer;

    protected AbstractSipMediaCommandExecutor(SipServer sipServer) {
        this.sipServer = sipServer;
    }

    public final SipServer getSipServer() {
        return this.sipServer;
    }

}
