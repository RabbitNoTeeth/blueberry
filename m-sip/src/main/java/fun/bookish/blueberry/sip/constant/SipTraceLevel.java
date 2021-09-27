package fun.bookish.blueberry.sip.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * sip日志级别
 */
public class SipTraceLevel {

    private static final Map<String, String> LEVEL_VALUE_MAP;

    /**
     * 默认的日志级别
     */
    private static final String DEFAULT_LEVEL_VALUE = "0";

    static {
        LEVEL_VALUE_MAP = new HashMap<>();
        LEVEL_VALUE_MAP.put("NONE", "0");
        LEVEL_VALUE_MAP.put("MESSAGE", "16");
        LEVEL_VALUE_MAP.put("EXCEPTION", "17");
        LEVEL_VALUE_MAP.put("DEBUG", "32");
    }

    private SipTraceLevel() {}

    /**
     * 将日志级别转换为sip api中识别的值
     * @param level
     * @return
     */
    public static String transformLevel(String level) {
        if (StringUtils.isBlank(level)) {
            return DEFAULT_LEVEL_VALUE;
        }
        String value = LEVEL_VALUE_MAP.get(level);
        if (StringUtils.isBlank(value)) {
            return DEFAULT_LEVEL_VALUE;
        }
        return value;
    }
}
