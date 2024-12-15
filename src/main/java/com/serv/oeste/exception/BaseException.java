package com.serv.oeste.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private HttpStatus status;
    private ExceptionResponse exceptionResponse;
}
