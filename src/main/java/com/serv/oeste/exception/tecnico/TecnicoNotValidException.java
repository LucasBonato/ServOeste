package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class TecnicoNotValidException extends BaseException {
    public TecnicoNotValidException(String message, Codigo IdError) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(IdError.getI(), message));
    }
}
