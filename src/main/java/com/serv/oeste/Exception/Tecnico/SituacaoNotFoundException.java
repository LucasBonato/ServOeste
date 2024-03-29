package com.serv.oeste.Exception.Tecnico;

import com.serv.oeste.Exception.BaseException;
import com.serv.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class SituacaoNotFoundException extends BaseException {
    public SituacaoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse("Situação do Técnico não encontrada ou inexistente!"));
    }
}
