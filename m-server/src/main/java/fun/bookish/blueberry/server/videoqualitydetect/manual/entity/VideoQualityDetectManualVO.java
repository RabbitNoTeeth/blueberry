package fun.bookish.blueberry.server.videoqualitydetect.manual.entity;

public class VideoQualityDetectManualVO {

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

