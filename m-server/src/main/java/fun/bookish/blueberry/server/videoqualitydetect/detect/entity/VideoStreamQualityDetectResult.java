package fun.bookish.blueberry.server.videoqualitydetect.detect.entity;

public class VideoStreamQualityDetectResult {

    private String deviceId;
    private String channelId;
    private Boolean hasError;
    private String error;
    private Boolean hasQualityError;
    private String qualityError;
    private String detail;
    private String snapshot;

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public Boolean getHasQualityError() {
        return hasQualityError;
    }

    public void setHasQualityError(Boolean hasQualityError) {
        this.hasQualityError = hasQualityError;
    }

    public String getQualityError() {
        return qualityError;
    }

    public void setQualityError(String qualityError) {
        this.qualityError = qualityError;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
