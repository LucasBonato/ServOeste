package com.serv.oeste.domain.exceptions.auth;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotValidException;

public class AuthTokenNotValidException extends NotValidException {
    public AuthTokenNotValidException() {
        super(ErrorFields.AUTH, "O token é inválido");
    }
}
