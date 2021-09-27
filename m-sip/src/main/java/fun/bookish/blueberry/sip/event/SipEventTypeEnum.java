package fun.bookish.blueberry.sip.event;

public enum SipEventTypeEnum {

    /**
     * 设备注册
     */
    DEVICE_REGISTER,
    /**
     * 设备注销
     */
    DEVICE_LOGOUT,
    /**
     * 设备信息响应
     */
    DEVICE_INFO_RESPONSE,
    /**
     * 设备状态响应
     */
    DEVICE_STATUS_RESPONSE,
    /**
     * 设备目录响应
     */
    CATALOG_RESPONSE,
    /**
     * 设备状态信息报送通知
     */
    KEEPALIVE_NOTIFY,
    /**
     * 媒体流点播成功
     */
    MEDIA_INVITE_SUCCESS;
}

