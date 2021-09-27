package fun.bookish.blueberry.server.schedule.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "video-quality-detect")
public class VideoQualityDetectProperties {

    /**
     * 支持的算法列表
     */
    private List<String> supportedArithmetics;

    public List<String> getSupportedArithmetics() {
        return supportedArithmetics;
    }

    public void setSupportedArithmetics(List<String> supportedArithmetics) {
        this.supportedArithmetics = supportedArithmetics;
    }
}
