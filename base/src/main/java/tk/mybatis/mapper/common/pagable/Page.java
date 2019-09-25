package tk.mybatis.mapper.common.pagable;

import java.util.List;

public class Page<T> {

    public static final int DEFAULT_PAGE_NO = 1;
    public static final int MAX_PAGE_SIZE = 500;

    private Integer totalItems;
    private Integer totalPages;
    private Integer pageNo;
    private Integer pageSize;
    private Integer pageItems;
    private List<T> items;

    public Page() { }

    public Page(List<T> items) {
        this.items = items;
    }

    public Page(List<T> items, Integer totalItems, Integer pageNo, Integer pageSize) {
        this.items = items;
        this.totalItems = totalItems;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        if (pageSize != null && pageSize > 0) {
            this.totalPages = totalItems / pageSize;
            if (totalItems % pageSize != 0) {
                this.totalPages = this.totalPages + 1;
            }
            this.pageItems = items == null ? 0 : items.size();
        } else {
            this.pageNo = 0;
            this.totalPages = 0;
            this.pageItems = 0;
        }
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageItems() {
        return pageItems;
    }

    public void setPageItems(Integer pageItems) {
        this.pageItems = pageItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
