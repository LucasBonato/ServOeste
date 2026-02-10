package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class TechnicianSpecialtyEmptyException extends NotValidException {
    public TechnicianSpecialtyEmptyException() {
        super(ErrorFields.CONHECIMENTO, "Técnico precisa possuir no mínimo uma especialidade!");
    }
}
