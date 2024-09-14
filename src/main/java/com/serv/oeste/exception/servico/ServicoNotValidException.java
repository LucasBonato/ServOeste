package com.serv.oeste.exception.servico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ServicoNotValidException extends BaseException {
    public ServicoNotValidException(Codigo codigo, String message) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(codigo.getI(), message));
    }
}
