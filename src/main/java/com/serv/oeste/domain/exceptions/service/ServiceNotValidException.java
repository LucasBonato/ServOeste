package com.serv.oeste.domain.exceptions.service;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class ServiceNotValidException extends NotValidException {
    public ServiceNotValidException(ErrorFields field, String message) {
        super(field, message);
    }
}
