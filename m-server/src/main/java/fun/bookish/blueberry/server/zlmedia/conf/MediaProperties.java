package fun.bookish.blueberry.server.zlmedia.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "media")
public class MediaProperties {

    private String ip;
    private Integer httpPort;
    private Integer rtpPort;
    private String secret;
    private Integer noReaderCheckInterval = 180;
    private String snapshotSavaPath = "./snapshots";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getRtpPort() {
        return rtpPort;
    }

    public void setRtpPort(Integer rtpPort) {
        this.rtpPort = rtpPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getNoReaderCheckInterval() {
        return noReaderCheckInterval;
    }

    public void setNoReaderCheckInterval(Integer noReaderCheckInterval) {
        this.noReaderCheckInterval = noReaderCheckInterval;
    }

    public String getSnapshotSavaPath() {
        return snapshotSavaPath;
    }

    public void setSnapshotSavaPath(String snapshotSavaPath) {
        this.snapshotSavaPath = snapshotSavaPath;
    }
}
