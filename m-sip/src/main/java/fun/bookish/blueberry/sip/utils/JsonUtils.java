package fun.bookish.blueberry.sip.utils;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * json序列化工具
 *
 * @author liuxindong
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static final Gson GSON = new GsonBuilder().create();

    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final JsonParser JSON_PARSER = new JsonParser();

    /**
     * 序列化
     *
     * @param data
     * @return
     */
    public static String encode(Object data) {
        return GSON.toJson(data);
    }

    /**
     * 反序列化
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T decode(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, clazz);
    }

    /**
     * 反序列化
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> decode2List(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        Type type = new ParameterizedTypeImpl(clazz);
        return GSON.fromJson(json, type);
    }

    /**
     * 反序列化
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T decode(String json, Type type) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, type);
    }

    /**
     * 反序列化
     *
     * @param json
     * @return
     */
    public static JsonObject decodeToJsonObject(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON_PARSER.parse(json).getAsJsonObject();
    }

    /**
     * 反序列化
     *
     * @param json
     * @return
     */
    public static JsonArray decodeToJsonArray(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON_PARSER.parse(json).getAsJsonArray();
    }

    /**
     * 类型转换
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return decode(encode(source), targetClass);
    }

    /**
     * 格式化json字符串
     * @param obj
     * @return
     */
    public static String toPrettyJson(Object obj) {
        return PRETTY_GSON.toJson(obj);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
