package com.serv.oeste.application.exceptions.client;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends BaseException {
    public ClientNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.CLIENTE.getI(), "Cliente não encontrado!"));
    }
}
