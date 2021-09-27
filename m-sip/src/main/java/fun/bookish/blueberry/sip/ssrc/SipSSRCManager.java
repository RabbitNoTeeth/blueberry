package fun.bookish.blueberry.sip.ssrc;

import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.ssrc.impl.SipSSRCManagerImpl;

import javax.sip.Dialog;
import javax.sip.SipException;

/**
 * SRRC管理器接口
 */
public interface SipSSRCManager {

    static SipSSRCManager create(SipServerConf sipServerConf){
        return new SipSSRCManagerImpl(sipServerConf);
    }

    /**
     * 生成实时视频点播SSRC
     *
     * @return
     */
    String generatePlaySSRC() throws SipException;

    /**
     * 生成历史视频点播SSRC
     *
     * @return
     */
    String generatePlayBackSSRC() throws SipException;

    /**
     * 释放ssrc
     * @param ssrc
     */
    void releaseSSRC(String ssrc);

    /**
     * 通过fromTag释放ssrc
     * @param fromTag
     */
    void releaseSSRCByFromTag(String fromTag);

    /**
     * 添加ssrc对应的请求tag
     * @param ssrc
     * @param fromTag
     */
    void addFromTag(String ssrc, String fromTag);

}
