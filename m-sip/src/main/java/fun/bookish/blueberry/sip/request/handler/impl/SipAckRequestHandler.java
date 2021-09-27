package fun.bookish.blueberry.sip.request.handler.impl;

import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.request.handler.AbstractSipRequestHandler;
import fun.bookish.blueberry.sip.utils.SipRequestUtils;
import gov.nist.javax.sip.header.CSeq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

/**
 * ACK请求处理器
 */
public class SipAckRequestHandler extends AbstractSipRequestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SipAckRequestHandler.class);

	@Override
	public void beforeHandle(SipRequestWrapper requestWrapper) {
		String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>> Receive an ACK request. From:{}", requestDesc);
		}
	}

	@Override
	public void doHandle(SipRequestWrapper requestWrapper) {
		RequestEvent requestEvent = requestWrapper.getRequestEvent();
		Request request = requestEvent.getRequest();
		String requestDesc = SipRequestUtils.getSimpleDesc(request);
		Dialog dialog = requestEvent.getDialog();
		try {
			CSeq csReq = (CSeq) request.getHeader(CSeq.NAME);
			Request ackRequest = dialog.createAck(csReq.getSeqNumber());
			dialog.sendAck(ackRequest);
			LOGGER.info("<<< Response the ACK Request. To:{}", requestDesc);
		} catch (Exception e) {
			LOGGER.error("(×) Failed to handle the ACK request. From:{}", requestDesc, e);
		}
	}

	@Override
	public void afterHandle(SipRequestWrapper requestWrapper) {

	}

}
