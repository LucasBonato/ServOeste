package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class EspecialidadeNotFoundException extends BaseException {
    public EspecialidadeNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.CONHECIMENTO.ordinal(), "Especialidade não encontrada!"));
    }
}
