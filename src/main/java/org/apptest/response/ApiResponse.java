package org.apptest.response;

public class ApiResponse<T> {
    public boolean status;
    public String message;
    public T data;

    public ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
