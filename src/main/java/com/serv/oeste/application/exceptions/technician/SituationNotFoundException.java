package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class SituationNotFoundException extends BaseException {
    public SituationNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Situação do Técnico não encontrada ou inexistente!"));
    }
}
