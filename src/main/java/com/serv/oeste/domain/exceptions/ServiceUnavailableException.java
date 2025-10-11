package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

public abstract class ServiceUnavailableException extends DomainException {
    public ServiceUnavailableException(ErrorFields field, String message) {
        super(field, message);
    }
}
