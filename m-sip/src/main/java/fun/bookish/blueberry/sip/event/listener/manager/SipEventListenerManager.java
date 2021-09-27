package fun.bookish.blueberry.sip.event.listener.manager;


import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.event.listener.SipEventListener;
import fun.bookish.blueberry.sip.event.listener.manager.impl.SipEventListenerManagerImpl;
import fun.bookish.blueberry.sip.event.SipEventContext;

import java.util.List;

/**
 * sip事件管理器接口
 */
public interface SipEventListenerManager {

    static SipEventListenerManager create(SipServer sipServer){
        return new SipEventListenerManagerImpl(sipServer);
    }

    /**
     * 添加事件监听
     * @param listener
     */
    void add(SipEventListener listener);

    /**
     * 添加事件监听
     * @param listeners
     */
    void addAll(List<SipEventListener> listeners);

    /**
     * 触发回调
     * @return
     */
    <T> void fire(SipEventContext<T> sipEventContext);

}
