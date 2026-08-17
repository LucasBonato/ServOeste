package com.serv.oeste.domain.exceptions.specialty;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class SpecialtyNameAlreadyUsedException extends NotValidException {
    public SpecialtyNameAlreadyUsedException() {
        super(ErrorFields.CONHECIMENTO, "Já existe uma especialidade com esse nome!");
    }
}