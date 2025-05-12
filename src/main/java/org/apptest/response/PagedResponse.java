package org.apptest.response;

public class PagedResponse<T> extends ApiResponse<T> {
    public Pagination pagination;

    public PagedResponse(boolean status, String message, T data, Pagination pagination) {
        super(status, message, data);
        this.pagination = pagination;
    }
}