package com.serv.oeste.application.exceptions.service;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ServiceNotValidException extends BaseException {
    public ServiceNotValidException(Codigo codigo, String message) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(codigo.getI(), message));
    }
}
