package fun.bookish.blueberry.server.sip.conf;

import fun.bookish.blueberry.sip.conf.SipServerConf;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sip")
public class SipProperties {

    /**
     * sip服务host
     */
    private String host;
    /**
     * sip服务端口
     */
    private Integer port;
    /**
     * sip服务id
     */
    private String id;
    /**
     * 根据国标6.1.2中规定，domain宜采用ID统一编码的前十位编码。国标附录D中定义前8位为中心编码（由省级、市级、区级、基层编号组成，参照GB/T 2260-2007）
     * 后两位为行业编码，定义参照附录D.3
     */
    private String domain;
    /**
     * sip服务名称
     */
    private String name;
    /**
     * sip服务密码（null或者空字符串表示不进行密码校验）
     */
    private String password;
    /**
     * sip日志级别：
     *      NONE
     *      MESSAGE
     *      EXCEPTION
     *      DEBUG
     */
    private String traceLevel = "NONE";
    /**
     * 是否日志打印消息内容
     */
    private Boolean logMessageContent = false;
    /**
     * 设备过期时长（s）
     *      默认使用设备请求中Expire时长，如果请求中未设置Expire，那么使用该时长作为设备过期时长
     */
    private Integer deviceExpires;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTraceLevel() {
        return traceLevel;
    }

    public void setTraceLevel(String traceLevel) {
        this.traceLevel = traceLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLogMessageContent() {
        return logMessageContent;
    }

    public void setLogMessageContent(Boolean logMessageContent) {
        this.logMessageContent = logMessageContent;
    }

    public Integer getDeviceExpires() {
        return deviceExpires;
    }

    public void setDeviceExpires(Integer deviceExpires) {
        this.deviceExpires = deviceExpires;
    }

    public String getAddress() {
        return this.host + ":" + this.port;
    }

    public SipServerConf mapToSipServerConf() {
        SipServerConf res = new SipServerConf();
        res.setName(name);
        res.setPort(port);
        res.setHost(host);
        res.setId(id);
        res.setDomain(domain);
        res.setPassword(password);
        res.setTraceLevel(traceLevel);
        res.setLogMessageContent(logMessageContent);
        res.setDeviceExpires(deviceExpires);
        return res;
    }

}
