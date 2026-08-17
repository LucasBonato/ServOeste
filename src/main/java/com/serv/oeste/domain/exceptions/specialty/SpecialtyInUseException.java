package com.serv.oeste.domain.exceptions.specialty;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class SpecialtyInUseException extends NotValidException {
    public SpecialtyInUseException() {
        super(ErrorFields.CONHECIMENTO, "Não é possível remover uma especialidade que está em uso por técnicos!");
    }
}