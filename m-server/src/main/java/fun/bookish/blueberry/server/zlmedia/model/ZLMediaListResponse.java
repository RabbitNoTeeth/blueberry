package fun.bookish.blueberry.server.zlmedia.model;

import java.util.List;

public class ZLMediaListResponse {

    private Integer code;

    private List<ZLMedia> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<ZLMedia> getData() {
        return data;
    }

    public void setData(List<ZLMedia> data) {
        this.data = data;
    }

    public static class ZLMedia {
        private String app;
        private Integer readerCount;
        private Integer totalReaderCount;
        private String schema;
        private String stream;
        private ZLMediaOriginSock originSock;
        private Integer originType;
        private String originTypeStr;
        private String originUrl;
        private Long createStamp;
        private Long aliveSecond;
        private Integer bytesSpeed;
        private List<ZLMediaTrack> tracks;
        private String vhost;

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public Integer getReaderCount() {
            return readerCount;
        }

        public void setReaderCount(Integer readerCount) {
            this.readerCount = readerCount;
        }

        public Integer getTotalReaderCount() {
            return totalReaderCount;
        }

        public void setTotalReaderCount(Integer totalReaderCount) {
            this.totalReaderCount = totalReaderCount;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getStream() {
            return stream;
        }

        public void setStream(String stream) {
            this.stream = stream;
        }

        public ZLMediaOriginSock getOriginSock() {
            return originSock;
        }

        public void setOriginSock(ZLMediaOriginSock originSock) {
            this.originSock = originSock;
        }

        public Integer getOriginType() {
            return originType;
        }

        public void setOriginType(Integer originType) {
            this.originType = originType;
        }

        public String getOriginTypeStr() {
            return originTypeStr;
        }

        public void setOriginTypeStr(String originTypeStr) {
            this.originTypeStr = originTypeStr;
        }

        public String getOriginUrl() {
            return originUrl;
        }

        public void setOriginUrl(String originUrl) {
            this.originUrl = originUrl;
        }

        public Long getCreateStamp() {
            return createStamp;
        }

        public void setCreateStamp(Long createStamp) {
            this.createStamp = createStamp;
        }

        public Long getAliveSecond() {
            return aliveSecond;
        }

        public void setAliveSecond(Long aliveSecond) {
            this.aliveSecond = aliveSecond;
        }

        public Integer getBytesSpeed() {
            return bytesSpeed;
        }

        public void setBytesSpeed(Integer bytesSpeed) {
            this.bytesSpeed = bytesSpeed;
        }

        public List<ZLMediaTrack> getTracks() {
            return tracks;
        }

        public void setTracks(List<ZLMediaTrack> tracks) {
            this.tracks = tracks;
        }

        public String getVhost() {
            return vhost;
        }

        public void setVhost(String vhost) {
            this.vhost = vhost;
        }
    }

    public static class ZLMediaOriginSock {
        private String identifier;
        private String local_ip;
        private Integer local_port;
        private String peer_ip;
        private Integer peer_port;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getLocal_ip() {
            return local_ip;
        }

        public void setLocal_ip(String local_ip) {
            this.local_ip = local_ip;
        }

        public Integer getLocal_port() {
            return local_port;
        }

        public void setLocal_port(Integer local_port) {
            this.local_port = local_port;
        }

        public String getPeer_ip() {
            return peer_ip;
        }

        public void setPeer_ip(String peer_ip) {
            this.peer_ip = peer_ip;
        }

        public Integer getPeer_port() {
            return peer_port;
        }

        public void setPeer_port(Integer peer_port) {
            this.peer_port = peer_port;
        }
    }

    public static class ZLMediaTrack {
        private Integer channels;
        private Integer codec_id;
        private String codec_id_name;
        private Integer codec_type;
        private Boolean ready;
        private Integer sample_bit;
        private Integer sample_rate;

        public Integer getChannels() {
            return channels;
        }

        public void setChannels(Integer channels) {
            this.channels = channels;
        }

        public Integer getCodec_id() {
            return codec_id;
        }

        public void setCodec_id(Integer codec_id) {
            this.codec_id = codec_id;
        }

        public String getCodec_id_name() {
            return codec_id_name;
        }

        public void setCodec_id_name(String codec_id_name) {
            this.codec_id_name = codec_id_name;
        }

        public Integer getCodec_type() {
            return codec_type;
        }

        public void setCodec_type(Integer codec_type) {
            this.codec_type = codec_type;
        }

        public Boolean getReady() {
            return ready;
        }

        public void setReady(Boolean ready) {
            this.ready = ready;
        }

        public Integer getSample_bit() {
            return sample_bit;
        }

        public void setSample_bit(Integer sample_bit) {
            this.sample_bit = sample_bit;
        }

        public Integer getSample_rate() {
            return sample_rate;
        }

        public void setSample_rate(Integer sample_rate) {
            this.sample_rate = sample_rate;
        }
    }

}
