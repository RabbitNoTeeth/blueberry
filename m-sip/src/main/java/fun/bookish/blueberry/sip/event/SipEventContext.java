package fun.bookish.blueberry.sip.event;

import fun.bookish.blueberry.sip.SipServer;

/**
 * sip事件上下文
 */
public class SipEventContext<T> {

    /**
     * 事件类型
     */
    private final SipEventTypeEnum sipEventTypeEnum;
    /**
     * sip服务
     */
    private final SipServer sipServer;
    /**
     * 数据
     */
    private final T data;

    public SipEventContext(SipEventTypeEnum sipEventTypeEnum, SipServer sipServer, T data) {
        this.sipEventTypeEnum = sipEventTypeEnum;
        this.sipServer = sipServer;
        this.data = data;
    }

    public SipEventTypeEnum getSipEventTypeEnum() {
        return sipEventTypeEnum;
    }

    public SipServer getSipServer() {
        return sipServer;
    }

    public T getData() {
        return data;
    }
}
