package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class EspecialidadeNotFoundException extends BaseException {
    public EspecialidadeNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Especialidade não encontrada!"));
    }
}
