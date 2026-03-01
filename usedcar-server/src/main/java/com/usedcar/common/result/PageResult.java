package com.usedcar.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private List<T> list;
    private long total;
    private int pageSize;
    private int currentPage;
    private int totalPages;

    public PageResult() {
    }

    public PageResult(List<T> list, long total, int pageSize, int currentPage) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}
