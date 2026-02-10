package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class TechnicianNotFoundException extends NotFoundException {
    public TechnicianNotFoundException() {
        super(ErrorFields.TECNICO, "Técnico não encontrado!");
    }
}
