package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

public abstract class InternalServerErrorException extends DomainException {
    public InternalServerErrorException(ErrorFields field, String message) {
        super(field, message);
    }
}
