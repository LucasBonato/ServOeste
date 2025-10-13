package com.serv.oeste.domain.exceptions.service;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

import java.util.List;
import java.util.Map;

public class ServiceNotValidException extends NotValidException {
    public ServiceNotValidException(ErrorFields field, String message) {
        super(field, message);
    }

    public ServiceNotValidException(Map<String, List<String>> errors) {
        super(errors);
    }
}
