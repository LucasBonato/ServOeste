package com.serv.oeste.application.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> baseExceptionHandler(BaseException exception){
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getExceptionResponse());
    }
}
