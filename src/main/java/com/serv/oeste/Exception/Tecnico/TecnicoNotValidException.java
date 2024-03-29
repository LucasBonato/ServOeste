package com.serv.oeste.Exception.Tecnico;

import com.serv.oeste.Exception.BaseException;
import com.serv.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TecnicoNotValidException extends BaseException {
    public TecnicoNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(message));
    }
}
