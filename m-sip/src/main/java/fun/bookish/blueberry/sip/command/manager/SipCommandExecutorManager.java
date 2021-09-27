package fun.bookish.blueberry.sip.command.manager;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.media.SipMediaCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.manager.impl.SipCommandExecutorManagerImpl;

/**
 * sip命令执行器管理器
 */
public interface SipCommandExecutorManager {

    static SipCommandExecutorManager create(SipServer sipServer) {
        return new SipCommandExecutorManagerImpl(sipServer);
    }

    SipServer getSipServer();

    SipQueryCommandExecutor getSipQueryCommandExecutor();

    SipMediaCommandExecutor getSipMediaCommandExecutor();

}
