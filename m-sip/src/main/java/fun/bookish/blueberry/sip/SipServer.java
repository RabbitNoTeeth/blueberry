package fun.bookish.blueberry.sip;

import fun.bookish.blueberry.sip.constant.SipTraceLevel;
import fun.bookish.blueberry.sip.event.listener.SipEventListener;
import fun.bookish.blueberry.sip.event.listener.manager.SipEventListenerManager;
import fun.bookish.blueberry.sip.command.manager.SipCommandExecutorManager;
import fun.bookish.blueberry.sip.conf.SipServerConf;
import fun.bookish.blueberry.sip.listener.SipServerListener;
import fun.bookish.blueberry.sip.request.producer.SipRequestProducer;
import fun.bookish.blueberry.sip.ssrc.SipSSRCManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.ListeningPoint;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * sip服务类
 */
public class SipServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipServer.class);

    private final SipServerConf sipServerConf;
    private SipFactory sipFactory;
    private SipStack sipStack;
    private SipProvider tcpSipProvider;
    private SipProvider udpSipProvider;
    private final SipRequestProducer sipRequestProducer;
    private final SipEventListenerManager sipEventListenerManager;
    private final SipCommandExecutorManager sipCommandExecutorManager;
    private final SipSSRCManager sipSSRCManager;

    private SipServer(SipServerConf sipServerConf, List<SipEventListener> listeners) {
        this.sipServerConf = sipServerConf;
        // 创建请求生产者
        this.sipRequestProducer = SipRequestProducer.create(this);
        // 创建事件监听管理器
        this.sipEventListenerManager = SipEventListenerManager.create(this);
        this.sipEventListenerManager.addAll(listeners);
        // 创建命令执行器管理器
        this.sipCommandExecutorManager = SipCommandExecutorManager.create(this);
        // 创建ssrc管理器
        sipSSRCManager = SipSSRCManager.create(sipServerConf);
    }

    public SipServerConf getSipServerConf() {
        return this.sipServerConf;
    }

    public SipFactory getSipFactory() {
        return this.sipFactory;
    }

    public SipStack getSipStack() {
        return this.sipStack;
    }

    public SipProvider getTcpSipProvider() {
        return this.tcpSipProvider;
    }

    public SipProvider getUdpSipProvider() {
        return this.udpSipProvider;
    }

    public SipRequestProducer getSipRequestProducer() {
        return this.sipRequestProducer;
    }

    public SipEventListenerManager getSipEventListenerManager() {
        return sipEventListenerManager;
    }

    public SipCommandExecutorManager getSipCommandExecutorManager() {
        return this.sipCommandExecutorManager;
    }

    public SipSSRCManager getSipSSRCManager() {
        return this.sipSSRCManager;
    }

    public void addEventListener(SipEventListener listener) {
        this.sipEventListenerManager.add(listener);
    }

    /**
     * 启动
     */
    public void start() {
        String ip = sipServerConf.getHost();
        Integer port = sipServerConf.getPort();
        String address = ip + ":" + port;
        try {
            // 启动sip server
            sipFactory = SipFactory.getInstance();
            Properties properties = new Properties();
            properties.setProperty("javax.sip.STACK_NAME", sipServerConf.getName());
            properties.setProperty("javax.sip.IP_ADDRESS", ip);
            properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", sipServerConf.getLogMessageContent().toString());
            properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", SipTraceLevel.transformLevel(sipServerConf.getTraceLevel()));
            properties.setProperty("gov.nist.javax.sip.SERVER_LOG", sipServerConf.getName() + "_server_log");
            properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", sipServerConf.getName() + "_debug_log");
            this.sipStack = sipFactory.createSipStack(properties);
            // 创建tcp服务
            ListeningPoint tcpListeningPoint = sipStack.createListeningPoint(ip, port, "TCP");
            this.tcpSipProvider = sipStack.createSipProvider(tcpListeningPoint);
            // 创建udp服务
            ListeningPoint udpListeningPoint = sipStack.createListeningPoint(ip, port, "UDP");
            this.udpSipProvider = sipStack.createSipProvider(udpListeningPoint);
            // 创建监听器
            SipServerListener serverListener = new SipServerListener(this);
            this.tcpSipProvider.addSipListener(serverListener);
            this.udpSipProvider.addSipListener(serverListener);
            LOGGER.info("(√) Succeeded in starting sip server. {} (tcp & udp)", address);
        } catch (Exception e) {
            throw new RuntimeException("(×) Failed to start sip server. " + address + " (tcp & udp)", e);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (sipStack != null) {
            sipStack.stop();
        }
    }

    public static class Builder {

        private final SipServerConf sipServerConf;

        private final List<SipEventListener> listeners = new ArrayList<>();

        public Builder(SipServerConf sipServerConf) {
            this.sipServerConf = sipServerConf;
        }

        /**
         * 添加事件监听器
         *
         * @param listener
         * @return
         */
        public Builder addEventListener(SipEventListener listener) {
            listeners.add(listener);
            return this;
        }

        /**
         * 构建sip服务
         * @return
         */
        public SipServer build() {
            return new SipServer(this.sipServerConf, this.listeners);
        }

    }

}
