package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

import java.util.List;
import java.util.Map;

public abstract class NotValidException extends DomainException {
    public NotValidException(ErrorFields field, String message) {
        super(field, message);
    }

    public NotValidException(Map<String, List<String>> fieldErrors) {
        super(fieldErrors);
    }
}
