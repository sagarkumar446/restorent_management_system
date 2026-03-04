package edu.qsp.restorent_management_system.dto;

public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private boolean success;

    public ApiResponse() {
    }

    public ApiResponse(int statusCode, String message, T data, boolean success) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
