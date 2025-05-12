package org.apptest.response;

public class Pagination {
    public long totalData;
    public int currentPage;
    public int nextPage;
    public int previousPage;
    public int pageSize;
    public int totalPage;

    public Pagination(long totalData, int currentPage, int pageSize) {
        this.totalData = totalData;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil((double) totalData / pageSize);
        this.previousPage = Math.max(currentPage - 1, 1);
        this.nextPage = Math.min(currentPage + 1, totalPage);
    }
}
