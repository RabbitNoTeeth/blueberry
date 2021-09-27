package fun.bookish.blueberry.server.zlmedia.connect;

import fun.bookish.blueberry.core.utils.HttpClientUtils;
import fun.bookish.blueberry.server.zlmedia.conf.MediaProperties;
import fun.bookish.blueberry.server.zlmedia.model.ZLMediaListResponse;
import fun.bookish.blueberry.server.zlmedia.model.ZLStreamAddStreamProxyResponse;
import fun.bookish.blueberry.server.zlmedia.model.ZLStreamCloseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ZLMediaKitConnection {

    @Autowired
    private MediaProperties mediaProperties;

    /**
     * 查询媒体服务器中的视频流列表
     *
     * @return
     */
    public ZLMediaListResponse queryStreamList() {
        // 请求ZLMedia媒体服务器，查询视频流列表
        String url = "http://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/index/api/getMediaList";
        String httpMethod = "GET";
        Map<String, Object> params = new HashMap<>();
        params.put("secret", mediaProperties.getSecret());
        params.put("schema", "rtmp");
        return HttpClientUtils.requestForEntity(url, httpMethod, null, params, ZLMediaListResponse.class);
    }

    /**
     * 关闭媒体服务器流
     *
     * @param streamId
     * @return
     */
    public ZLStreamCloseResponse closeStream(String streamId) {
        // 请求ZLMedia媒体服务器，查询视频流列表
        String url = "http://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/index/api/close_streams";
        String httpMethod = "GET";
        Map<String, Object> params = new HashMap<>();
        params.put("secret", mediaProperties.getSecret());
        params.put("stream", streamId);
        return HttpClientUtils.requestForEntity(url, httpMethod, null, params, ZLStreamCloseResponse.class);
    }

    /**
     * 查询视频截图
     * @param streamUrl
     * @return
     */
    public byte[] querySnap(String streamUrl) {
        String url = "http://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/index/api/getSnap";
        String httpMethod = "GET";
        Map<String, Object> params = new HashMap<>();
        params.put("secret", mediaProperties.getSecret());
        params.put("url", streamUrl);
        params.put("timeout_sec", 5);
        params.put("expire_sec", 1);
        return HttpClientUtils.requestForByteArray(url, httpMethod, null, params);
    }

    /**
     * 添加拉流代理
     * @return
     */
    public ZLStreamAddStreamProxyResponse addRtspStreamProxy(String app, String streamId, String url) {
        String url_ = "http://" + mediaProperties.getIp() + ":" + mediaProperties.getHttpPort() + "/index/api/addStreamProxy";
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("secret", mediaProperties.getSecret());
        paramMap.put("vhost", "__defaultVhost__");
        paramMap.put("app", app);
        paramMap.put("stream", streamId);
        paramMap.put("url", url);
        return HttpClientUtils.requestForEntity(url_, "post", headerMap, paramMap, ZLStreamAddStreamProxyResponse.class);
    }

}
