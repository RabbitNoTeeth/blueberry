package fun.bookish.blueberry.sip.response.handler.impl;

import fun.bookish.blueberry.sip.response.handler.AbstractSipResponseHandler;
import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BYE响应处理器
 */
public class SipByeResponseHandler extends AbstractSipResponseHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SipByeResponseHandler.class);

	@Override
	public void beforeHandle(SipResponseWrapper responseWrapper) {

	}

	@Override
	public void doHandle(SipResponseWrapper responseWrapper) {
		// todo 触发相关事件，以便客户端能知道关流是否成功
	}

	@Override
	public void afterHandle(SipResponseWrapper responseWrapper) {

	}
}
