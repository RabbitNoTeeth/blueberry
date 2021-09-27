package fun.bookish.blueberry.sip.command.executor.query;


import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceInfoParam;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryDeviceStatusParam;
import fun.bookish.blueberry.sip.command.executor.SipCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.impl.SipQueryCommandExecutorImpl;
import fun.bookish.blueberry.sip.exception.SipCommandExecuteException;

/**
 * sip查询命令执行器接口
 */
public interface SipQueryCommandExecutor extends SipCommandExecutor {

	static SipQueryCommandExecutor create(SipServer sipServer){
		return new SipQueryCommandExecutorImpl(sipServer);
	}

	/**
	 * 查询设备信息
	 * @param sipQueryDeviceInfoParam
	 */
	void queryDeviceInfo(SipQueryDeviceInfoParam sipQueryDeviceInfoParam) throws SipCommandExecuteException;

	/**
	 * 查询设备状态
	 * @param sipQueryDeviceStatusParam
	 */
	void queryDeviceStatus(SipQueryDeviceStatusParam sipQueryDeviceStatusParam) throws SipCommandExecuteException;

	/**
	 * 查询设备目录
	 * @param sipQueryCatalogParam
	 */
	void queryCatalog(SipQueryCatalogParam sipQueryCatalogParam) throws SipCommandExecuteException;

}
