package fun.bookish.blueberry.sip.event.listener;

import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.entity.*;

/**
 * sip事件回调处理器接口，可根据需要重写感兴趣的事件处理逻辑
 */
public interface SipEventListener {

    /**
     * 设备注册
     *
     * @param sipEventContext
     */
    default void deviceRegister(SipEventContext<SipDeviceRegisterDesc> sipEventContext) {
    }


    /**
     * 设备注销
     *
     * @param sipEventContext
     */
    default void deviceLogout(SipEventContext<String> sipEventContext) {
    }


    /**
     * 设备信息响应
     *
     * @param sipEventContext
     */
    default void deviceInfoResponse(SipEventContext<SipDeviceInfoDesc> sipEventContext) {
    }

    /**
     * 设备状态响应
     *
     * @param sipEventContext
     */
    default void deviceStatusResponse(SipEventContext<SipDeviceStatusDesc> sipEventContext) {
    }

    /**
     * 设备目录响应
     *
     * @param sipEventContext
     */
    default void catalogResponse(SipEventContext<SipCatalogDesc> sipEventContext) {
    }

    /**
     * 设备状态信息报送通知
     *
     * @param sipEventContext
     */
    default void keepaliveNotify(SipEventContext<SipKeepaliveDesc> sipEventContext) {
    }

    /**
     * 媒体流点播成功
     *
     * @param sipEventContext
     */
    default void mediaInviteSuccess(SipEventContext<SipMediaInviteDesc> sipEventContext) {
    }
}
