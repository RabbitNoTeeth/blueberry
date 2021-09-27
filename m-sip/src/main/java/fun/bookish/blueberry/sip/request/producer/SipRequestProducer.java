package fun.bookish.blueberry.sip.request.producer;

import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.exception.SipRequestCreationException;
import fun.bookish.blueberry.sip.request.producer.impl.SipRequestProducerImpl;

import javax.sip.message.Request;

/**
 * sip请求生产者接口
 */
public interface SipRequestProducer {

    static SipRequestProducer create(SipServer sipServer) {
        return new SipRequestProducerImpl(sipServer);
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
    Request createMessageRequest(String toDeviceId, String toAddress, String deviceId, String transport, String content) throws SipRequestCreationException;

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
    Request createInviteRequest(String toDeviceId, String toAddress, String deviceId, String transport, String content) throws SipRequestCreationException;

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
    Request createInviteRequest(String toDeviceId, String toAddress, String deviceId, String transport, String fromTag, String content) throws SipRequestCreationException;

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
    Request createByeRequest(String toDeviceId, String toAddress, String deviceId, String transport, String fromTag, String toTag, String callId) throws SipRequestCreationException;

}
