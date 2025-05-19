package com.serv.oeste.application.exceptions.client;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ClientNotValidException extends BaseException {
    public ClientNotValidException(String message, Codigo codigo) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(codigo, message));
    }
}
