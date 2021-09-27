package fun.bookish.blueberry.sip.request.handler.impl;

import fun.bookish.blueberry.sip.event.listener.manager.SipEventListenerManager;
import fun.bookish.blueberry.sip.digest.MessageDigestTypeEnum;
import fun.bookish.blueberry.sip.event.SipEventContext;
import fun.bookish.blueberry.sip.event.SipEventTypeEnum;
import fun.bookish.blueberry.sip.exception.SipDeviceRegisterException;
import fun.bookish.blueberry.sip.entity.SipDeviceRegisterDesc;
import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.request.handler.AbstractSipRequestHandler;
import fun.bookish.blueberry.sip.utils.MessageDigestUtils;
import fun.bookish.blueberry.sip.utils.SipHeaderUtils;
import fun.bookish.blueberry.sip.utils.SipRequestUtils;
import gov.nist.javax.sip.header.Expires;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipFactory;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Calendar;
import java.util.Locale;

/**
 * REGISTER请求处理器
 */
public class SipRegisterRequestHandlerImpl extends AbstractSipRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipRegisterRequestHandlerImpl.class);

    @Override
    public void beforeHandle(SipRequestWrapper requestWrapper) throws Exception{
        String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" >>> Receive a REGISTER request. From:{}", requestDesc);
        }
    }

    @Override
    public void doHandle(SipRequestWrapper requestWrapper) throws Exception{
        // 进行身份认证
        boolean success = doIdentityAuthentication(requestWrapper);
        requestWrapper.setData(success);
    }

    @Override
    public void afterHandle(SipRequestWrapper requestWrapper) throws Exception {
        String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
        boolean success = (boolean) requestWrapper.getData();
        if (success) {
            SipDeviceRegisterDesc deviceDesc = createDeviceDesc(requestWrapper);
            SipEventListenerManager listenerManager = requestWrapper.getSipServer().getSipEventListenerManager();
            String deviceID = deviceDesc.getDeviceID();
            if (deviceDesc.getExpires() == 0) {
                SipEventTypeEnum eventType = SipEventTypeEnum.DEVICE_LOGOUT;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Fire the sip event '{}'. From:{}", eventType, requestDesc);
                }
                // 触发设备注销事件监听
                listenerManager.fire(new SipEventContext<>(eventType, requestWrapper.getSipServer(), deviceID));
            } else {
                // 触发设备注册事件监听
                SipEventTypeEnum eventType = SipEventTypeEnum.DEVICE_REGISTER;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Fire the sip event '{}'. From:{}", eventType, deviceDesc);
                }
                listenerManager.fire(new SipEventContext<>(eventType, requestWrapper.getSipServer(), deviceDesc));
            }
        }
    }

    /**
     * 进行身份认证
     * @param requestWrapper
     * @return
     */
    private boolean doIdentityAuthentication(SipRequestWrapper requestWrapper) throws SipDeviceRegisterException {
        String requestDesc = SipRequestUtils.getSimpleDesc(requestWrapper.getRequestEvent().getRequest());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Authenticating the device identity. From:{}", requestDesc);
        }
        RequestEvent requestEvent = requestWrapper.getRequestEvent();
        Request request = requestEvent.getRequest();
        SipFactory sipFactory = requestWrapper.getSipServer().getSipFactory();
        SipServerConf sipServerConf = requestWrapper.getSipServer().getSipServerConf();
        try {
            MessageFactory messageFactory = sipFactory.createMessageFactory();
            HeaderFactory headerFactory = sipFactory.createHeaderFactory();
            ServerTransaction serverTransaction = getServerTransaction(requestWrapper);
            // 获取请求头中的身份认证信息
            AuthorizationHeader authorizationHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
            if (authorizationHeader == null) {
                // 未携带身份认证信息，直接响应 UNAUTHORIZED
                Response response = messageFactory.createResponse(Response.UNAUTHORIZED, request);
                response.addHeader(SipHeaderUtils.createWWWAuthenticateHeader(sipFactory, MessageDigestTypeEnum.MD5, sipServerConf.getDomain()));
                serverTransaction.sendResponse(response);
                LOGGER.info("<<< Response the DeviceRegister whit UNAUTHORIZED, case of no AuthorizationHeader. To:{}", requestDesc);
                return false;
            }
            // 获取sip服务配置密码
            String confPassword = sipServerConf.getPassword();
            if (StringUtils.isNotBlank(confPassword)) {
                // 已配置密码，进行校验
                if (!authPassword(requestWrapper, confPassword)) {
                    // 校验失败，响应 UNAUTHORIZED
                    Response response = messageFactory.createResponse(Response.UNAUTHORIZED, request);
                    response.addHeader(SipHeaderUtils.createWWWAuthenticateHeader(sipFactory, MessageDigestTypeEnum.MD5, sipServerConf.getDomain()));
                    serverTransaction.sendResponse(response);
                    LOGGER.info("<<< Response the DeviceRegister whit UNAUTHORIZED, case of invalid identity. To:{}", requestDesc);
                    return false;
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("There is no need to authenticate device's identity. From:{}", requestDesc);
                }
            }
            // 执行到此处，说明身份认证通过（要么是sip服务未配置密码，要么是密码验证通过）
            // 创建OK响应
            Response response = messageFactory.createResponse(Response.OK, request);
            // 设置date请求头
            response.addHeader(headerFactory.createDateHeader(Calendar.getInstance(Locale.ENGLISH)));
            // 设置expires请求头
            response.addHeader(request.getExpires());
            // 发回 OK 响应
            serverTransaction.sendResponse(response);
            // 打印日志
            ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(Expires.NAME);
            if (expiresHeader.getExpires() != 0) {
                LOGGER.info("(√) Device register successfully! From:{}", requestDesc);
            } else {
                LOGGER.info("(√) Device logout successfully! From:{}", requestDesc);
            }
            return true;
        } catch (Exception e) {
            throw new SipDeviceRegisterException("(×) Failed to authenticate device's identity. From:{" + requestDesc + "}", e);
        }
    }

    /**
     * 进行密码校验
     *
     * @param requestWrapper
     * @param confPassword
     * @return
     */
    private boolean authPassword(SipRequestWrapper requestWrapper, String confPassword) {
        Request request = requestWrapper.getRequestEvent().getRequest();
        AuthorizationHeader authHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();
        if (StringUtils.isBlank(realm) || StringUtils.isBlank(username)) {
            return false;
        }
        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            return false;
        }
        String A1 = username + ":" + realm + ":" + confPassword;
        String A2 = request.getMethod().toUpperCase() + ":" + uri;
        byte[] mdbytes = MessageDigestUtils.digest(MessageDigestTypeEnum.MD5, A1.getBytes());
        String HA1 = MessageDigestUtils.toHexString(mdbytes);

        mdbytes = MessageDigestUtils.digest(MessageDigestTypeEnum.MD5, A2.getBytes());
        String HA2 = MessageDigestUtils.toHexString(mdbytes);

        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        mdbytes = MessageDigestUtils.digest(MessageDigestTypeEnum.MD5, KD.getBytes());
        String mdString = MessageDigestUtils.toHexString(mdbytes);
        String response = authHeader.getResponse();
        return mdString.equals(response);
    }

    /**
     * 创建设备描述
     *
     * @param requestWrapper
     * @return
     */
    private SipDeviceRegisterDesc createDeviceDesc(SipRequestWrapper requestWrapper) {
        SipServerConf sipServerConf = requestWrapper.getSipServer().getSipServerConf();
        Request request = requestWrapper.getRequestEvent().getRequest();
        // 获取请求来源设备id及地址
        String requestDeviceId = SipRequestUtils.getFromDeviceId(request);
        String requestDeviceAddress = SipRequestUtils.getFromDeviceAddress(request);
        // 获取设备过期时长，如果设备请求中未设置过期时长，那么使用sip配置中设置的过期时长
        ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(Expires.NAME);
        int expires = expiresHeader == null ? sipServerConf.getDeviceExpires() : expiresHeader.getExpires();
        // 创建设备注册描述
        String registerServer = sipServerConf.getAddress();
        SipEventTypeEnum eventType = expires == 0 ? SipEventTypeEnum.DEVICE_LOGOUT : SipEventTypeEnum.DEVICE_REGISTER;
        SipDeviceRegisterDesc sipDeviceRegisterDesc = new SipDeviceRegisterDesc(requestDeviceId, requestDeviceAddress, requestDeviceId, registerServer, eventType);
        sipDeviceRegisterDesc.setRegisterAddress(requestDeviceAddress);
        sipDeviceRegisterDesc.setTransport(SipRequestUtils.getTransport(request));
        sipDeviceRegisterDesc.setExpires(expires);
        return sipDeviceRegisterDesc;
    }
}
