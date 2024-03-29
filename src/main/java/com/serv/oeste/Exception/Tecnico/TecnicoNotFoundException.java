package com.serv.oeste.Exception.Tecnico;

import com.serv.oeste.Exception.BaseException;
import com.serv.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TecnicoNotFoundException extends BaseException {
    public TecnicoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse("Técnico não encontrado!"));
    }
}
