package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TecnicoNotFoundException extends BaseException {
    public TecnicoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Técnico não encontrado!"));
    }
}
