package com.example.cryptocurrency_test_task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NotSupportedCurrencyException extends RuntimeException {

    public NotSupportedCurrencyException(String message) {
        super(message);
    }

}
