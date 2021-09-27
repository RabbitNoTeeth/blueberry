package fun.bookish.blueberry.sip.response.handler.impl;

import fun.bookish.blueberry.sip.response.handler.AbstractSipResponseHandler;
import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 未知请求处理器
 */
public class SipUnknownResponseHandler extends AbstractSipResponseHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SipUnknownResponseHandler.class);

	@Override
	public void beforeHandle(SipResponseWrapper responseWrapper) {

	}

	@Override
	public void doHandle(SipResponseWrapper responseWrapper) {
		LOGGER.warn("暂不支持未知类型消息处理...");
	}

	@Override
	public void afterHandle(SipResponseWrapper responseWrapper) {

	}
}
