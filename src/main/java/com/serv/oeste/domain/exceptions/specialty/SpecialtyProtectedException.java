package com.serv.oeste.domain.exceptions.specialty;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class SpecialtyProtectedException extends NotValidException {
    public SpecialtyProtectedException() {
        super(ErrorFields.CONHECIMENTO, "A especialidade 'Outros' não pode ser alterada ou removida!");
    }
}