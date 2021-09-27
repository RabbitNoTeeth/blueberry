package fun.bookish.blueberry.core.json;

/**
 * http json 响应实体
 *
 * @author LiuXinDong
 */
public class JsonResult {

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 信息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    private JsonResult() {
    }

    public static JsonResult of(boolean success, String successMessage, String failMessage) {
        JsonResult res = new JsonResult();
        res.setSuccess(success);
        res.setMessage(success ? successMessage : failMessage);
        return res;
    }

    public static JsonResult success(String message) {
        JsonResult res = new JsonResult();
        res.setSuccess(true);
        res.setMessage(message);
        return res;
    }

    public static JsonResult success(String message, Object data) {
        JsonResult res = new JsonResult();
        res.setSuccess(true);
        res.setMessage(message);
        res.setData(data);
        return res;
    }

    public static JsonResult fail(String message) {
        JsonResult res = new JsonResult();
        res.setSuccess(false);
        res.setMessage(message);
        return res;
    }

    public static JsonResult fail(String message, Object data) {
        JsonResult res = new JsonResult();
        res.setSuccess(false);
        res.setMessage(message);
        res.setData(data);
        return res;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResult setData(Object data) {
        this.data = data;
        return this;
    }

    public JsonResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }
}
