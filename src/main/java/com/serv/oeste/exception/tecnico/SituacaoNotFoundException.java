package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class SituacaoNotFoundException extends BaseException {
    public SituacaoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Situação do Técnico não encontrada ou inexistente!"));
    }
}
