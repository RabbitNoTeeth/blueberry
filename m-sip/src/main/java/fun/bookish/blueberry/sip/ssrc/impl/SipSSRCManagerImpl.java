package fun.bookish.blueberry.sip.ssrc.impl;

import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.ssrc.SipSSRCManager;

import javax.sip.SipException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRRC管理器实现
 */
public class SipSSRCManagerImpl implements SipSSRCManager {

    private final SipServerConf sipServerConf;

    private final List<String> ssrcs = new ArrayList<>();

    private final Map<String, String> fromTag_map = new ConcurrentHashMap<>();

    public SipSSRCManagerImpl(SipServerConf sipServerConf) {
        this.sipServerConf = sipServerConf;
    }

    /**
     * 生成实时视频点播SSRC
     *
     * @return
     */
    @Override
    public String generatePlaySSRC() throws SipException {
        return generateSSRC("0");
    }

    /**
     * 生成历史视频点播SSRC
     *
     * @return
     */
    @Override
    public String generatePlayBackSSRC() throws SipException {
        return generateSSRC("1");
    }

    /**
     * 释放ssrc
     *
     * @param ssrc
     */
    @Override
    public synchronized void releaseSSRC(String ssrc) {
        String key = ssrc.split(parseSipId())[1];
        ssrcs.remove(key);
        fromTag_map.remove(key);
    }

    /**
     * 通过fromTag释放ssrc
     * @param fromTag
     */
    @Override
    public void releaseSSRCByFromTag(String fromTag) {
        String ssrc = null;
        Set<Map.Entry<String, String>> entries = fromTag_map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (fromTag.equals(entry.getValue())) {
                ssrc = entry.getKey();
                break;
            }
        }
        if (ssrc != null) {
            ssrcs.remove(ssrc);
            fromTag_map.remove(ssrc);
        }
    }

    /**
     * 添加ssrc对应的请求tag
     * @param ssrc
     * @param fromTag
     */
    @Override
    public void addFromTag(String ssrc, String fromTag) {
        fromTag_map.put(ssrc.split(parseSipId())[1], fromTag);
    }

    /**
     * 生成点播SSRC
     * 十进制整数字符串,表示 SSRC 值。格式如下: dddddddddd 。
     * 其中,第 1 位为历史或实时媒体流的标识位,0 为实时, 1 为历史;
     * 第 2 位至第 6 位取 20 位 SIP 监控域 ID 之中的 4 到 8 位作为域标识,例如“13010000002000000001 ”中取数字“ 10000 ”;
     * 第 7 位至第 10 位作为域内媒体流标识,是一个与当前域内产生的媒体流 SSRC 值后 4 位不重复的四位十进制整数。
     *
     * @return
     */
    private synchronized String generateSSRC(String type) throws SipException {
        if (ssrcs.size() >= 10000) {
            throw new SipException("there is no available ssrc");
        }
        String r = random();
        while (ssrcs.contains(r)) {
            r = random();
        }
        // todo 当ssrc数量较大时，例如已经打到9000+，那么循环取随机数可能还不如从1到10000循环查找快，目前不考虑该情况，留待后续优化
        return type + parseSipId() + complete(r);
    }

    /**
     * 获取ssrc前缀
     *
     * @return
     */
    private String parseSipId() {
        return this.sipServerConf.getId().substring(3, 8);
    }

    /**
     * 补全
     *
     * @return
     */
    private String complete(String i) {
        int length = i.length();
        String s;
        if (length < 2) {
            s = "000" + i;
        } else if (length < 3) {
            s = "00" + i;
        } else if (length < 4) {
            s = "0" + i;
        } else {
            s = "" + i;
        }
        return s;
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private String random() {
        return (int) (Math.random() * 10000) + "";
    }

}
