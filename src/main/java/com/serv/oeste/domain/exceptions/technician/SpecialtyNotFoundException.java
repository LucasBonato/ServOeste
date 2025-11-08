package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class SpecialtyNotFoundException extends NotFoundException {
    public SpecialtyNotFoundException() {
        super(ErrorFields.CONHECIMENTO, "Especialidade não encontrada!");
    }
}
