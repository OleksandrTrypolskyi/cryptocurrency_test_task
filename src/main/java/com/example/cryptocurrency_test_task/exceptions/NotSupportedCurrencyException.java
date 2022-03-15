package com.example.cryptocurrency_test_task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NotSupportedCurrencyException extends RuntimeException {
    public NotSupportedCurrencyException() {
        super();
    }

    public NotSupportedCurrencyException(String message) {
        super(message);
    }

    public NotSupportedCurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
