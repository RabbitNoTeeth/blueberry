package fun.bookish.blueberry.sip.command.executor.media;


import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.SipCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.media.impl.SipMediaCommandExecutorImpl;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaPlayParam;
import fun.bookish.blueberry.sip.command.executor.media.param.SipMediaStopParam;
import fun.bookish.blueberry.sip.exception.SipCommandExecuteException;

/**
 * sip查询命令执行器接口
 */
public interface SipMediaCommandExecutor extends SipCommandExecutor {

	static SipMediaCommandExecutor create(SipServer sipServer){
		return new SipMediaCommandExecutorImpl(sipServer);
	}

	/**
	 * 点播实时媒体流
	 * @param param
	 * @return
	 */
	void playStart(SipMediaPlayParam param) throws SipCommandExecuteException;

	/**
	 * 停止实时媒体流
	 * @param param
	 * @return
	 */
	void playStop(SipMediaStopParam param) throws SipCommandExecuteException;

}
