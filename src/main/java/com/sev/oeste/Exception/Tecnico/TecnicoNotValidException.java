package com.sev.oeste.Exception.Tecnico;

import com.sev.oeste.Exception.BaseException;
import com.sev.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class TecnicoNotValidException extends BaseException {
    public TecnicoNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(message));
    }
}
