package fun.bookish.blueberry.sip.event.listener.manager.impl;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.event.listener.SipEventListener;
import fun.bookish.blueberry.sip.event.listener.manager.SipEventListenerManager;
import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.event.SipEventTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * sip事件管理器接口实现
 */
public class SipEventListenerManagerImpl implements SipEventListenerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipEventListenerManagerImpl.class);

    private final CopyOnWriteArrayList<SipEventListener> listeners;

    private final ExecutorService executorService;

    private final SipServer sipServer;

    public SipEventListenerManagerImpl(SipServer sipServer) {
        this.sipServer = sipServer;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * 添加回调处理器
     *
     * @param callback
     */
    @Override
    public void add(SipEventListener callback) {
        this.listeners.add(callback);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("(√) Succeed in adding a sip event listener. listener:{}", callback.getClass().getTypeName());
        }
    }

    @Override
    public void addAll(List<SipEventListener> listeners) {
        this.listeners.addAll(listeners);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("(√) Succeed in adding a sip event listener. listener:{}", listeners.stream().map(l -> l.getClass().getSimpleName()).collect(Collectors.joining(",")));
        }
    }

    /**
     * 触发回调
     *
     * @param sipEventContext
     */
    @Override
    public void fire(SipEventContext sipEventContext) {
        // 使用线程池调用每个回调处理器，一是保证不阻塞主线程池，二是保证各回调处理器能够尽快执行
        listeners.forEach(callback -> this.executorService.execute(() -> {
            SipEventTypeEnum sipEventTypeEnum = sipEventContext.getSipEventTypeEnum();
            try {
                switch (sipEventTypeEnum) {
                    case DEVICE_REGISTER:
                        callback.deviceRegister(sipEventContext);
                        break;
                    case DEVICE_LOGOUT:
                        callback.deviceLogout(sipEventContext);
                        break;
                    case DEVICE_INFO_RESPONSE:
                        callback.deviceInfoResponse(sipEventContext);
                        break;
                    case DEVICE_STATUS_RESPONSE:
                        callback.deviceStatusResponse(sipEventContext);
                        break;
                    case CATALOG_RESPONSE:
                        callback.catalogResponse(sipEventContext);
                        break;
                    case KEEPALIVE_NOTIFY:
                        callback.keepaliveNotify(sipEventContext);
                        break;
                    case MEDIA_INVITE_SUCCESS:
                        callback.mediaInviteSuccess(sipEventContext);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                LOGGER.error("(×) Failed to fire sip event. event:{}", sipEventTypeEnum, e);
            }
        }));
    }

}
