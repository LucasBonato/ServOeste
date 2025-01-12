package com.serv.oeste.exception.servico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ServicoNotFoundException extends BaseException {
    public ServicoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.SERVICO, "Serviço não encontrado"));
    }
}
