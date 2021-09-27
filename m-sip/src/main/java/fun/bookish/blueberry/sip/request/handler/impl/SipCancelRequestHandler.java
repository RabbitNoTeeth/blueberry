package fun.bookish.blueberry.sip.request.handler.impl;

import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.request.handler.AbstractSipRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CANCEL请求处理器
 */
public class SipCancelRequestHandler extends AbstractSipRequestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SipCancelRequestHandler.class);

	@Override
	public void beforeHandle(SipRequestWrapper requestWrapper) {

	}

	@Override
	public void doHandle(SipRequestWrapper requestWrapper) {
		// todo 实现CANCEL类型消息处理
		LOGGER.warn("暂未实现{}类型消息处理...", requestWrapper.getRequestEvent().getRequest().getMethod());
	}

	@Override
	public void afterHandle(SipRequestWrapper requestWrapper) {

	}

}
