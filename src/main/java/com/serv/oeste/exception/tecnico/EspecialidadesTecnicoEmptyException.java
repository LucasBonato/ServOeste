package com.serv.oeste.exception.tecnico;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class EspecialidadesTecnicoEmptyException extends BaseException {
    public EspecialidadesTecnicoEmptyException() {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(Codigo.CONHECIMENTO.ordinal(),"Técnico precisa possuir no mínimo uma especialidade!"));
    }
}
