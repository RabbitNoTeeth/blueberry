package fun.bookish.blueberry.server.schedule.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "video-no-reader-check")
public class VideoNoReaderCheckProperties {

    /**
     * 无人观看状态的视频流存活时间
     */
    private int aliveSeconds = 180;

    public int getAliveSeconds() {
        return aliveSeconds;
    }

    public void setAliveSeconds(int aliveSeconds) {
        this.aliveSeconds = aliveSeconds;
    }
}
