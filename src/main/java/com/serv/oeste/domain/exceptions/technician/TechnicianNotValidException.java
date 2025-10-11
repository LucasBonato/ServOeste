package com.serv.oeste.domain.exceptions.technician;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class TechnicianNotValidException extends NotValidException {
    public TechnicianNotValidException(ErrorFields field, String message) {
        super(field, message);
    }
}
