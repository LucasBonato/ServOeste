package com.serv.oeste.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class BaseException extends RuntimeException{
    private HttpStatus status;
    private ExceptionResponse exceptionResponse;
}
