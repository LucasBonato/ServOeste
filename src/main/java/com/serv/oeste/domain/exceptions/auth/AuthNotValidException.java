package com.serv.oeste.domain.exceptions.auth;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class AuthNotValidException extends NotValidException {
    public AuthNotValidException(String message) {
        super(ErrorFields.AUTH, message);
    }
}
