package com.astrapay.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

class ExampleAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(Exception.class)
    public void handleConflict() {
        // Nothing to do
    }
}