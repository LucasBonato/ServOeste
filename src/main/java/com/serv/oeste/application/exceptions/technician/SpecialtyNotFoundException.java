package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class SpecialtyNotFoundException extends BaseException {
    public SpecialtyNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.CONHECIMENTO.ordinal(), "Especialidade não encontrada!"));
    }
}
