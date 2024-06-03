package com.serv.oeste.exception.cliente;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ClienteNotValidException extends BaseException {
    public ClienteNotValidException(String message, Codigo codigo) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(codigo.ordinal(), message));
    }
}
