package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.exceptions.ErrorInfo;
import com.example.cryptocurrency_test_task.exceptions.NotSupportedCurrencyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotSupportedCurrencyException.class)
    public @ResponseBody
    ErrorInfo notSupportedCurrencyException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NumberFormatException.class)
    public @ResponseBody
    ErrorInfo numberFormatException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }
}
