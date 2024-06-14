package com.serv.oeste.exception.cliente;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ClienteNotFoundException extends BaseException {
    public ClienteNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.CLIENTE.getI(), "Cliente não encontrado!"));
    }
}
