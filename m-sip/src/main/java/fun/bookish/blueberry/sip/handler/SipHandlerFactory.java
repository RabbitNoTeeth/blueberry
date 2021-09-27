package fun.bookish.blueberry.sip.handler;

import fun.bookish.blueberry.sip.request.handler.SipRequestHandler;
import fun.bookish.blueberry.sip.request.handler.impl.*;
import fun.bookish.blueberry.sip.response.handler.SipResponseHandler;
import fun.bookish.blueberry.sip.response.handler.impl.SipInviteResponseHandler;
import fun.bookish.blueberry.sip.response.handler.impl.SipUnknownResponseHandler;

import javax.sip.message.Request;
import java.util.HashMap;
import java.util.Map;

/**
 * sip处理器工厂
 */
public class SipHandlerFactory {

    private static final Map<String, SipRequestHandler> REQUEST_HANDLER_MAP = new HashMap<>();

    private static final Map<String, SipResponseHandler> RESPONSE_HANDLER_MAP = new HashMap<>();

    private static final SipUnknownRequestHandler UNKNOWN_REQUEST_HANDLER = new SipUnknownRequestHandler();

    private static final SipUnknownResponseHandler UNKNOWN_RESPONSE_HANDLER = new SipUnknownResponseHandler();

    static {
        REQUEST_HANDLER_MAP.put(Request.ACK, new SipAckRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.BYE, new SipByeRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.CANCEL, new SipCancelRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.INVITE, new SipInviteRequestHandler());
        RESPONSE_HANDLER_MAP.put(Request.INVITE, new SipInviteResponseHandler());
        REQUEST_HANDLER_MAP.put(Request.OPTIONS, new SipOptionsRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.REGISTER, new SipRegisterRequestHandlerImpl());
        REQUEST_HANDLER_MAP.put(Request.NOTIFY, new SipNotifyRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.SUBSCRIBE, new SipSubscribeRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.MESSAGE, new SipMessageRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.REFER, new SipReferRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.INFO, new SipInfoRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.PRACK, new SipPrackRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.UPDATE, new SipUpdateRequestHandler());
        REQUEST_HANDLER_MAP.put(Request.PUBLISH, new SipPublishRequestHandler());
    }

    private SipHandlerFactory() {}

    /**
     * 根据请求方法返回对应的消息处理器
     * @param method
     * @return
     */
    public static SipRequestHandler getRequestHandler(String method) {
        SipRequestHandler targetHandler = REQUEST_HANDLER_MAP.get(method);
        return targetHandler == null ? UNKNOWN_REQUEST_HANDLER : targetHandler;
    }

    /**
     * 根据响应方法返回对应的消息处理器
     * @param method
     * @return
     */
    public static SipResponseHandler getResponseHandler(String method) {
        SipResponseHandler targetHandler = RESPONSE_HANDLER_MAP.get(method);
        return targetHandler == null ? UNKNOWN_RESPONSE_HANDLER : targetHandler;
    }
}
