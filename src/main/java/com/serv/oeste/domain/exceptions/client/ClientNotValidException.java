package com.serv.oeste.domain.exceptions.client;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

import java.util.List;
import java.util.Map;

public class ClientNotValidException extends NotValidException {
    public ClientNotValidException(ErrorFields field, String message) {
        super(field, message);
    }

    public ClientNotValidException(Map<String, List<String>> fieldErrors) {
        super(fieldErrors);
    }
}
