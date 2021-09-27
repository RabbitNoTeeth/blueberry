package fun.bookish.blueberry.sip.request.producer.impl;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.exception.SipRequestCreationException;
import fun.bookish.blueberry.sip.request.producer.SipRequestProducer;
import fun.bookish.blueberry.sip.utils.SipTagUtils;
import gov.nist.javax.sip.header.CallID;

import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.List;

/**
 * sip请求生产者接口实现
 */
public class SipRequestProducerImpl implements SipRequestProducer {

    private final SipServer sipServer;

    public SipRequestProducerImpl(SipServer sipServer) {
        this.sipServer = sipServer;
    }

    /**
     * 创建MESSAGE请求
     *
     * @param toDeviceId
     * @param toAddress
     * @param deviceId
     * @param transport
     * @param content
     * @return
     * @throws SipRequestCreationException
     */
    @Override
    public Request createMessageRequest(String toDeviceId, String toAddress, String deviceId, String transport, String content) throws SipRequestCreationException {
        try {
            SipFactory sipFactory = sipServer.getSipFactory();
            SipProvider tcpSipProvider = sipServer.getTcpSipProvider();
            SipProvider udpSipProvider = sipServer.getUdpSipProvider();
            SipServerConf sipServerConf = sipServer.getSipServerConf();
            String sipServerId = sipServerConf.getId();
            String sipServerDomain = sipServerConf.getDomain();
            String sipServerHost = sipServerConf.getHost();
            Integer sipServerPort = sipServerConf.getPort();
            String sipServerAddress = sipServerConf.getAddress();
            // requestURI
            SipURI requestURI = sipFactory.createAddressFactory().createSipURI(toDeviceId, toAddress);
            // via
            List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
            ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(sipServerHost, sipServerPort, transport, null);
            viaHeader.setRPort();
            viaHeaders.add(viaHeader);
            // from
            SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress);
            Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
            FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, SipTagUtils.random());
            // to
            SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(deviceId, sipServerDomain);
            Address toHeaderAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
            ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toHeaderAddress, null);
            // callid
            CallIdHeader callIdHeader = "TCP".equals(transport) ? tcpSipProvider.getNewCallId() : udpSipProvider.getNewCallId();
            // Forwards
            MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
            // ceq
            CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(1L, Request.MESSAGE);
            Request request = sipFactory.createMessageFactory().createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
            // concat
            Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress));
            request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

            ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
            request.setContent(content, contentTypeHeader);

            return request;
        } catch (Exception e) {
            throw new SipRequestCreationException("(×) Failed to create a MESSAGE request", e);
        }
    }

    /**
     * 创建INVITE请求
     *
     * @param toDeviceId
     * @param toAddress
     * @param deviceId
     * @param transport
     * @param content
     * @return
     * @throws SipRequestCreationException
     */
    @Override
    public Request createInviteRequest(String toDeviceId, String toAddress, String deviceId, String transport, String content) throws SipRequestCreationException {
        return createInviteRequest(toDeviceId, toAddress, deviceId, transport, SipTagUtils.random(), content);
    }

    /**
     * 创建INVITE请求
     *
     * @param toDeviceId
     * @param toAddress
     * @param deviceId
     * @param transport
     * @param fromTag
     * @param content
     * @return
     * @throws SipRequestCreationException
     */
    @Override
    public Request createInviteRequest(String toDeviceId, String toAddress, String deviceId, String transport, String fromTag, String content) throws SipRequestCreationException {
        try {
            SipFactory sipFactory = sipServer.getSipFactory();
            SipProvider tcpSipProvider = sipServer.getTcpSipProvider();
            SipProvider udpSipProvider = sipServer.getUdpSipProvider();
            SipServerConf sipServerConf = sipServer.getSipServerConf();
            String sipServerId = sipServerConf.getId();
            String sipServerDomain = sipServerConf.getDomain();
            String sipServerHost = sipServerConf.getHost();
            Integer sipServerPort = sipServerConf.getPort();
            String sipServerAddress = sipServerConf.getAddress();
            // requestURI
            SipURI requestURI = sipFactory.createAddressFactory().createSipURI(toDeviceId, toAddress);
            // via
            List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
            ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(sipServerHost, sipServerPort, transport, null);
            viaHeader.setRPort();
            viaHeaders.add(viaHeader);
            // from
            SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress);
            Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
            FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
            // to
            SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(deviceId, sipServerDomain);
            Address toHeaderAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
            ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toHeaderAddress, null);
            // callid
            CallIdHeader callIdHeader = "TCP".equals(transport) ? tcpSipProvider.getNewCallId() : udpSipProvider.getNewCallId();
            // Forwards
            MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
            // ceq
            CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(1L, Request.INVITE);
            Request request = sipFactory.createMessageFactory().createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
            // concat
            Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress));
            request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

            ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("Application", "SDP");
            request.setContent(content, contentTypeHeader);

            return request;
        } catch (Exception e) {
            throw new SipRequestCreationException("(×) Failed to create an INVITE request", e);
        }
    }

    /**
     * 创建BYE请求
     *
     * @param toDeviceId
     * @param toAddress
     * @param deviceId
     * @param transport
     * @param fromTag
     * @param toTag
     * @param callId
     * @return
     * @throws SipRequestCreationException
     */
    @Override
    public Request createByeRequest(String toDeviceId, String toAddress, String deviceId, String transport, String fromTag, String toTag, String callId) throws SipRequestCreationException {
        try {
            SipFactory sipFactory = sipServer.getSipFactory();
            SipProvider tcpSipProvider = sipServer.getTcpSipProvider();
            SipProvider udpSipProvider = sipServer.getUdpSipProvider();
            SipServerConf sipServerConf = sipServer.getSipServerConf();
            String sipServerId = sipServerConf.getId();
            String sipServerDomain = sipServerConf.getDomain();
            String sipServerHost = sipServerConf.getHost();
            Integer sipServerPort = sipServerConf.getPort();
            String sipServerAddress = sipServerConf.getAddress();
            // requestURI
            SipURI requestURI = sipFactory.createAddressFactory().createSipURI(toDeviceId, toAddress);
            // via
            List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
            ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(sipServerHost, sipServerPort, transport, null);
            viaHeader.setRPort();
            viaHeaders.add(viaHeader);
            // from
            SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress);
            Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
            FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
            // to
            SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(deviceId, sipServerDomain);
            Address toHeaderAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
            ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toHeaderAddress, toTag);
            // callid
            CallIdHeader callIdHeader = new CallID(callId);
            // Forwards
            MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
            // ceq
            CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(2L, Request.BYE);
            Request request = sipFactory.createMessageFactory().createRequest(requestURI, Request.BYE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
            // concat
            Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory().createSipURI(sipServerId, sipServerAddress));
            request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

            return request;
        } catch (Exception e) {
            throw new SipRequestCreationException("(×) Failed to create a BYE request", e);
        }
    }

}
