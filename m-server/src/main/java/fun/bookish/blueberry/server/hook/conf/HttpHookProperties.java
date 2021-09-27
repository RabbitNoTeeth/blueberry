package fun.bookish.blueberry.server.hook.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "http-hook")
public class HttpHookProperties {

    private Boolean enable;
    private Integer timeout = 10;
    private String onVideoQualityDetect;
    private String onDeviceOnline;
    private String onDeviceOffline;
    private String onDeviceCreated;
    private String onDeviceUpdated;
    private String onDeviceRemoved;
    private String onChannelOnline;
    private String onChannelOffline;
    private String onChannelCreated;
    private String onChannelUpdated;
    private String onChannelRemoved;

    public String getOnVideoQualityDetect() {
        return onVideoQualityDetect;
    }

    public void setOnVideoQualityDetect(String onVideoQualityDetect) {
        this.onVideoQualityDetect = onVideoQualityDetect;
    }

    public String getOnDeviceOnline() {
        return onDeviceOnline;
    }

    public void setOnDeviceOnline(String onDeviceOnline) {
        this.onDeviceOnline = onDeviceOnline;
    }

    public String getOnDeviceOffline() {
        return onDeviceOffline;
    }

    public void setOnDeviceOffline(String onDeviceOffline) {
        this.onDeviceOffline = onDeviceOffline;
    }

    public String getOnDeviceUpdated() {
        return onDeviceUpdated;
    }

    public void setOnDeviceUpdated(String onDeviceUpdated) {
        this.onDeviceUpdated = onDeviceUpdated;
    }

    public String getOnChannelUpdated() {
        return onChannelUpdated;
    }

    public void setOnChannelUpdated(String onChannelUpdated) {
        this.onChannelUpdated = onChannelUpdated;
    }

    public String getOnDeviceRemoved() {
        return onDeviceRemoved;
    }

    public void setOnDeviceRemoved(String onDeviceRemoved) {
        this.onDeviceRemoved = onDeviceRemoved;
    }

    public String getOnChannelRemoved() {
        return onChannelRemoved;
    }

    public void setOnChannelRemoved(String onChannelRemoved) {
        this.onChannelRemoved = onChannelRemoved;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getOnChannelOnline() {
        return onChannelOnline;
    }

    public void setOnChannelOnline(String onChannelOnline) {
        this.onChannelOnline = onChannelOnline;
    }

    public String getOnChannelOffline() {
        return onChannelOffline;
    }

    public void setOnChannelOffline(String onChannelOffline) {
        this.onChannelOffline = onChannelOffline;
    }

    public String getOnDeviceCreated() {
        return onDeviceCreated;
    }

    public void setOnDeviceCreated(String onDeviceCreated) {
        this.onDeviceCreated = onDeviceCreated;
    }

    public String getOnChannelCreated() {
        return onChannelCreated;
    }

    public void setOnChannelCreated(String onChannelCreated) {
        this.onChannelCreated = onChannelCreated;
    }
}
