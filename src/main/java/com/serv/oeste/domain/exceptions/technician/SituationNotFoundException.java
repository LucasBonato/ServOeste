package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class SituationNotFoundException extends NotFoundException {
    public SituationNotFoundException() {
        super(ErrorFields.SITUACAO, "Situação do Técnico não encontrada ou inexistente!");
    }
}
