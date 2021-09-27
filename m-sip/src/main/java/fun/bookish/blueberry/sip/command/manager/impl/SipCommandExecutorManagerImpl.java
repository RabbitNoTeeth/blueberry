package fun.bookish.blueberry.sip.command.manager.impl;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.media.SipMediaCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.manager.SipCommandExecutorManager;

/**
 * sip命令执行器管理器
 */
public class SipCommandExecutorManagerImpl implements SipCommandExecutorManager {

    private final SipServer sipServer;
    private final SipQueryCommandExecutor sipQueryCommandExecutor;
    private final SipMediaCommandExecutor sipMediaCommandExecutor;

    public SipCommandExecutorManagerImpl(SipServer sipServer) {
        this.sipServer = sipServer;
        this.sipQueryCommandExecutor = SipQueryCommandExecutor.create(sipServer);
        this.sipMediaCommandExecutor = SipMediaCommandExecutor.create(sipServer);
    }

    @Override
    public SipServer getSipServer() {
        return this.sipServer;
    }

    @Override
    public SipQueryCommandExecutor getSipQueryCommandExecutor() {
        return this.sipQueryCommandExecutor;
    }

    @Override
    public SipMediaCommandExecutor getSipMediaCommandExecutor() {
        return this.sipMediaCommandExecutor;
    }
}
