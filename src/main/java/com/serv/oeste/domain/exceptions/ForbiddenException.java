package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

public abstract class ForbiddenException extends DomainException {
    public ForbiddenException(ErrorFields field, String message) {
        super(field, message);
    }
}
