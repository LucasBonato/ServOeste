package com.sev.oeste.Exception.Tecnico;

import com.sev.oeste.Exception.BaseException;
import com.sev.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class EspecialidadeNotFoundException extends BaseException {
    public EspecialidadeNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse("Especialidade não encontrada!"));
    }
}
