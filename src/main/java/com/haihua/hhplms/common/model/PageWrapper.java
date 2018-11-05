package com.haihua.hhplms.common.model;

public class PageWrapper<T> {
    private T result;
    private int pageSize;
    private int pageNo;
    private int totalCount;

    public PageWrapper(int pageNo, int pageSize, int totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageCount() {
        if (totalCount <= 0) {
            return 0;
        } else {
            return totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        }
    }
}
