package com.serv.oeste.application.exceptions.service;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ServiceNotFoundException extends BaseException {
    public ServiceNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.SERVICO, "Serviço não encontrado"));
    }
}
