package com.serv.oeste.application.exceptions;

import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class RestTemplateException extends BaseException {
    public RestTemplateException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ExceptionResponse(Codigo.ENDERECO.getI(), message));
    }
}
