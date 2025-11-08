package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class TechnicianAlreadyDisabledException extends NotValidException {
    public TechnicianAlreadyDisabledException() {
        super(ErrorFields.TECNICO, "Técnico já está desativado");
    }
}
