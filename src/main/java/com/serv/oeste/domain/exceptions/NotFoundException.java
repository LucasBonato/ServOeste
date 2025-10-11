package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

public abstract class NotFoundException extends DomainException {
    public NotFoundException(ErrorFields field, String message) {
        super(field, message);
    }
}
