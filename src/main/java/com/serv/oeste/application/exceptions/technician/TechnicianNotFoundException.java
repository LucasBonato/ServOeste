package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TechnicianNotFoundException extends BaseException {
    public TechnicianNotFoundException() {
        super(HttpStatus.NOT_FOUND, new ExceptionResponse(0, "Técnico não encontrado!"));
    }
}
