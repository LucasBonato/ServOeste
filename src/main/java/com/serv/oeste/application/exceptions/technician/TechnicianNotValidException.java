package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class TechnicianNotValidException extends BaseException {
    public TechnicianNotValidException(String message, Codigo IdError) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(IdError.getI(), message));
    }
}
