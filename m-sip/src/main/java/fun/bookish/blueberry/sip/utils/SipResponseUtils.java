package fun.bookish.blueberry.sip.utils;

import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.response.model.SipResponseWrapper;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.To;
import org.apache.commons.lang3.StringUtils;

import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Response;

/**
 * sip响应工具类
 */
public class SipResponseUtils {

    private SipResponseUtils() {}

    /**
     * 获取请求的简短描述
     * @return
     * @param response
     */
    public  static String getSimpleDesc(Response response) {
        ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
        AddressImpl address = (AddressImpl) toHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return "<" + uri.getUser() + ">";
    }

    /**
     * 获取设备id
     * @return
     * @param response
     */
    public  static String getFromDeviceId(Response response) {
        ContactHeader contactHeader = (ContactHeader) response.getHeader(ContactHeader.NAME);
        if (contactHeader != null) {
            AddressImpl address = (AddressImpl) contactHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getUser();
        } else {
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            AddressImpl address = (AddressImpl) toHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getUser();
        }
    }

    /**
     * 获取通道id
     * @return
     * @param response
     */
    public  static String getChannelId(Response response) {
        ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
        AddressImpl address = (AddressImpl) toHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return uri.getUser();
    }

    /**
     * 获取设备地址
     * @return
     * @param response
     */
    public  static String getFromAddress(Response response) {
        ContactHeader contactHeader = (ContactHeader) response.getHeader(ContactHeader.NAME);
        if (contactHeader != null) {
            AddressImpl address = (AddressImpl) contactHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            return uri.getHostPort().encode();
        } else {
            ViaHeader viaHeader = (ViaHeader) response.getHeader(ViaHeader.NAME);
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
     * @param response
     * @return
     */
    public static String getTransport(Response response) {
        ViaHeader reqViaHeader = (ViaHeader) response.getHeader(ViaHeader.NAME);
        return reqViaHeader.getTransport();
    }

    /**
     * 获取当前sip服务地址
     * @param responseWrapper
     * @return
     */
    public static String getSipServerAddress(SipResponseWrapper responseWrapper) {
        SipServerConf sipServerConf = responseWrapper.getSipServer().getSipServerConf();
        return sipServerConf.getHost() + ":" + sipServerConf.getPort();
    }
}
