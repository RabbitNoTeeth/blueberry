package fun.bookish.blueberry.server.zlmedia.model;

public class ZLStreamCloseResponse {

    private Integer code;
    private Integer count_hit;
    private Integer count_closed;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount_hit() {
        return count_hit;
    }

    public void setCount_hit(Integer count_hit) {
        this.count_hit = count_hit;
    }

    public Integer getCount_closed() {
        return count_closed;
    }

    public void setCount_closed(Integer count_closed) {
        this.count_closed = count_closed;
    }
}
