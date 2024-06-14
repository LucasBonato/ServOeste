package com.serv.oeste.exception.viacep;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class RestTemplateException extends BaseException {
    public RestTemplateException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ExceptionResponse(Codigo.ENDERECO.getI(), message));
    }
}
