package fun.bookish.blueberry.sip.response.model;

import fun.bookish.blueberry.sip.SipServer;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;

/**
 * sip响应包装类
 */
public class SipResponseWrapper {

    private final SipServer sipServer;

    private final ResponseEvent responseEvent;

    private Object data;

    public SipResponseWrapper(SipServer sipServer, ResponseEvent responseEvent) {
        this.sipServer = sipServer;
        this.responseEvent = responseEvent;
    }

    public SipServer getSipServer() {
        return sipServer;
    }

    public ResponseEvent getResponseEvent() {
        return responseEvent;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

}
