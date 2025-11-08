package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

public abstract class UnauthorizedException extends DomainException {
    public UnauthorizedException(ErrorFields field, String message) {
        super(field, message);
    }
}
