package com.serv.oeste.application.exceptions;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ProblemDetail exceptionsHandler(BaseException exception){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        exception.getStatus(),
                        exception.getExceptionResponse().getMessage()
                );

        Map<String, Object> properties = new HashMap<>();
        properties.put("error", exception.getExceptionResponse());

        problemDetail.setProperties(properties);

        return problemDetail;
    }
}
