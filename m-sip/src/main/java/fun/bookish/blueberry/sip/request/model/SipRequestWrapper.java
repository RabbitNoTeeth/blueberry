package fun.bookish.blueberry.sip.request.model;


import fun.bookish.blueberry.sip.SipServer;

import javax.sip.*;

/**
 * sip请求包装类
 */
public class SipRequestWrapper {

    private final SipServer sipServer;

    private final RequestEvent requestEvent;

    private Object data;

    public SipRequestWrapper(SipServer sipServer, RequestEvent requestEvent) {
        this.sipServer = sipServer;
        this.requestEvent = requestEvent;
    }

    public SipServer getSipServer() {
        return sipServer;
    }

    public RequestEvent getRequestEvent() {
        return requestEvent;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
