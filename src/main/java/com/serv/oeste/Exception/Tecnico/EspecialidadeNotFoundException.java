package com.serv.oeste.Exception.Tecnico;

import com.serv.oeste.Exception.BaseException;
import com.serv.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class EspecialidadeNotFoundException extends BaseException {
    public EspecialidadeNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Especialidade não encontrada!"));
    }
}
