package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class EspecialidadesTecnicoEmptyException extends BaseException {
    public EspecialidadesTecnicoEmptyException() {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(5,"Técnico precisa possuir no mínimo uma especialidade!"));
    }
}
