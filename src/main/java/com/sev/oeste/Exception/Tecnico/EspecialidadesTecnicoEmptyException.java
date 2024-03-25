package com.sev.oeste.Exception.Tecnico;

import com.sev.oeste.Exception.BaseException;
import com.sev.oeste.Exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class EspecialidadesTecnicoEmptyException extends BaseException {
    public EspecialidadesTecnicoEmptyException() {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse("Técnico precisa possuir no mínimo uma especialidade!"));
    }
}
