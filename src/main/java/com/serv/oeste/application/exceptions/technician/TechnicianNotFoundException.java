package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class TechnicianNotFoundException extends BaseException {
    public TechnicianNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(Codigo.TECNICO, "Técnico não encontrado!"));
    }
}
