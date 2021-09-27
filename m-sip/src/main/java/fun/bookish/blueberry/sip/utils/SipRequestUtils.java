package fun.bookish.blueberry.sip.utils;

import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.request.model.SipRequestWrapper;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import org.apache.commons.lang3.StringUtils;

import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

/**
 * sip请求工具类
 */
public class SipRequestUtils {

    private SipRequestUtils() {
    }

    /**
     * 获取请求的简短描述
     *
     * @param request
     * @return
     */
    public static String getSimpleDesc(Request request) {
        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
        AddressImpl address = (AddressImpl) fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return "<" + uri.getUser() + ">";
    }

    /**
     * 获取设备地址
     *
     * @param request
     * @return
     */
    public static String getFromDeviceId(Request request) {
        ContactHeader contactHeader = (ContactHeader) request.getHeader(ContactHeader.NAME);
        if (contactHeader != null) {
            AddressImpl address = (AddressImpl) contactHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getUser();
        } else {
            FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
            AddressImpl address = (AddressImpl) fromHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getUser();
        }
    }

    /**
     * 获取通道id
     *
     * @param request
     * @return
     */
    public static String getChannelId(Request request) {
        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
        AddressImpl address = (AddressImpl) fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return uri.getUser();
    }

    /**
     * 获取设备地址
     *
     * @param request
     * @return
     */
    public static String getFromDeviceAddress(Request request) {
        ContactHeader contactHeader = (ContactHeader) request.getHeader(ContactHeader.NAME);
        if (contactHeader != null) {
            AddressImpl address = (AddressImpl) contactHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getHostPort().encode();
        } else {
            ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
            String ip = viaHeader.getReceived();
            int port = viaHeader.getRPort();
            if (StringUtils.isBlank(ip) || port == -1) {
                ip = viaHeader.getHost();
                port = viaHeader.getPort();
            }
            return ip.concat(":").concat(port + "");
        }
    }

    /**
     * 获取传输协议类型
     *
     * @param request
     * @return
     */
    public static String getTransport(Request request) {
        ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        return reqViaHeader.getTransport();
    }

    /**
     * 获取当前sip服务地址
     *
     * @param requestWrapper
     * @return
     */
    public static String getSipServerAddress(SipRequestWrapper requestWrapper) {
        SipServerConf sipServerConf = requestWrapper.getSipServer().getSipServerConf();
        return sipServerConf.getAddress();
    }
}
