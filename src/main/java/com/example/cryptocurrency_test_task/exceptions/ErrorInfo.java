package com.example.cryptocurrency_test_task.exceptions;

public class ErrorInfo {
    public String url;
    public String message;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.message = ex.getMessage();
    }
}
