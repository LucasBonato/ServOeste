package com.sev.oeste.Exception.Tecnico;

import com.sev.oeste.Exception.BaseException;
import com.sev.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TecnicoNotFoundException extends BaseException {
    public TecnicoNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse("Técnico não encontrado!"));
    }
}
