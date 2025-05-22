package com.serv.oeste.application.exceptions.technician;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class TechnicianSpecialtyEmptyException extends BaseException {
    public TechnicianSpecialtyEmptyException() {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(Codigo.CONHECIMENTO.ordinal(),"Técnico precisa possuir no mínimo uma especialidade!"));
    }
}
