package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity;

import java.util.List;

public class VideoQualityDetectArithmeticApplicableDeviceVO {

    private String code;
    private String name;
    private List<VideoQualityDetectArithmeticApplicableDeviceVO> children;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoQualityDetectArithmeticApplicableDeviceVO> getChildren() {
        return children;
    }

    public void setChildren(List<VideoQualityDetectArithmeticApplicableDeviceVO> children) {
        this.children = children;
    }

}
