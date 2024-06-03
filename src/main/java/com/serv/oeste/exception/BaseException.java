package com.serv.oeste.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class BaseException extends RuntimeException{
    private HttpStatus status;
    private ExceptionResponse exceptionResponse;
}
