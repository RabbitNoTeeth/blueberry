package fun.bookish.blueberry.core.page;

import java.util.List;

public class PageResult<E> {

    private long total;

    private List<E> data;

    public PageResult() {
    }

    public PageResult(long total, List<E> data) {
        this.total = total;
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
