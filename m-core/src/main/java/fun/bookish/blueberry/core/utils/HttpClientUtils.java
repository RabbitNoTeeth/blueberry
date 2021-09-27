package fun.bookish.blueberry.core.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

/**
 * http客户端工具类
 */
public class HttpClientUtils {

    public static final String HTTP_METHOD_GET = "GET";

    public static final String HTTP_METHOD_POST = "POST";

    public static final String HTTP_METHOD_PUT = "PUT";

    public static final String HTTP_METHOD_DELETE = "DELETE";

    private HttpClientUtils() {
    }

    /**
     * 发起http请求，以指定实体类格式返回response响应体中的数据
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @param entityClass   实体类class
     * @return
     */
    public static <T> T requestForEntity(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params,
                                         Class<T> entityClass) {
        return requestForEntity(url, requestMethod, headers, params, null, entityClass);
    }

    /**
     * 发起http请求，以指定实体类格式返回response响应体中的数据
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @param charsetName   字符集
     * @param entityClass   实体类class
     * @return
     */
    public static <T> T requestForEntity(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params,
                                         String charsetName, Class<T> entityClass) {
        String responseString = requestForString(url, requestMethod, headers, params, charsetName);
        return JsonUtils.decode(responseString, entityClass);
    }

    /**
     * 发起http请求，以字符串格式返回response响应体中的数据
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @return
     */
    public static String requestForString(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params) {
        return requestForString(url, requestMethod, headers, params, null);
    }

    /**
     * 发起http请求，以字符串格式返回response响应体中的数据
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @param charsetName   字符集
     * @return
     */
    public static String requestForString(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params, String charsetName) {
        try {
            byte[] byteArray = requestForByteArray(url, requestMethod, headers, params);
            if (StringUtils.isNotBlank(charsetName)) {
                return new String(byteArray, charsetName);
            } else {
                return new String(byteArray);
            }
        } catch (Exception e) {
            throw new RestHttpRequestException("requestForString failed", e);
        }
    }

    /**
     * 发起http请求，以字节数组格式返回response响应体中的数据
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @return
     */
    public static byte[] requestForByteArray(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params) {
        try {
            HttpResponse response = requestForResponse(url, requestMethod, headers, params);
            byte[] responseBody = EntityUtils.toByteArray(response.getEntity());
            StatusLine responseStatusLine = response.getStatusLine();
            int responseStatusCode = responseStatusLine.getStatusCode();
            // 如果非正常响应，直接抛出异常
            if (responseStatusCode >= 500) {
                throw new RestHttpRequestException("http request failed: status = " + responseStatusCode + ", reason = " + new String(responseBody));
            }
            // 响应成功，返回响应体内容
            return responseBody;
        } catch (Exception e) {
            throw new RestHttpRequestException("requestForByteArray failed", e);
        }
    }

    /**
     * 发起http请求，返回response响应
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param headers       请求头
     * @param params        请求参数
     * @return
     */
    public static HttpResponse requestForResponse(String url, String requestMethod, Map<String, String> headers, Map<String, Object> params) {
        return requestForResponse(url, requestMethod, null,
                uri -> {
                    if (params != null && params.size() > 0) {
                        params.forEach((k, v) -> uri.addParameter(k, v.toString()));
                    }
                }, request -> {
                    if (headers != null && headers.size() > 0) {
                        headers.forEach(request::addHeader);
                    }
                });
    }

    /**
     * 发起http请求，返回response响应
     *
     * @param url           接口地址
     * @param requestMethod 请求方法
     * @param httpClientBuilderConsumer       HttpClientBuilder消费者
     * @return
     */
    public static HttpResponse requestForResponse(String url, String requestMethod,
                                                  Consumer<HttpClientBuilder> httpClientBuilderConsumer,
                                                  Consumer<URIBuilder> uriBuilderConsumer,
                                                  Consumer<HttpUriRequest> requestConsumer) {
        try {
            if (StringUtils.isBlank(requestMethod)) {
                throw new RestHttpRequestException("invalid http request method:" + requestMethod);
            }
            requestMethod = requestMethod.trim().toUpperCase();
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            // 自定义HttpClient相关属性
            if (httpClientBuilderConsumer != null) {
                httpClientBuilderConsumer.accept(httpClientBuilder);
            }
            HttpClient httpClient = httpClientBuilder.build();
            URIBuilder uriBuilder = new URIBuilder(url);
            // 自定义uri相关属性
            if (uriBuilderConsumer != null) {
                uriBuilderConsumer.accept(uriBuilder);
            }
            URI uri = uriBuilder.build();
            HttpUriRequest httpRequest;
            switch (requestMethod) {
                case "GET":
                    httpRequest = new HttpGet(uri);
                    break;
                case "POST":
                    httpRequest = new HttpPost(uri);
                    break;
                case "PUT":
                    httpRequest = new HttpPut(uri);
                    break;
                case "DELETE":
                    httpRequest = new HttpDelete(uri);
                    break;
                default:
                    throw new RestHttpRequestException("invalid http request method:" + requestMethod);
            }
            // 自定义request相关属性
            if (requestConsumer != null) {
                requestConsumer.accept(httpRequest);
            }
            return httpClient.execute(httpRequest);
        } catch (Exception e) {
            throw new RestHttpRequestException("requestForResponse failed", e);
        }
    }

    static class RestHttpRequestException extends RuntimeException{

        private static final long serialVersionUID = -3518662549793452324L;

        RestHttpRequestException(){
            super();
        }

        RestHttpRequestException(String message){
            super(message);
        }

        RestHttpRequestException(String message, Throwable e){
            super(message, e);
        }

    }

}
