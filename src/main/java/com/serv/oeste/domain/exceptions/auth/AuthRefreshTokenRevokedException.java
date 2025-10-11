package com.serv.oeste.domain.exceptions.auth;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.ForbiddenException;

public class AuthRefreshTokenRevokedException extends ForbiddenException {
    public AuthRefreshTokenRevokedException() {
        super(ErrorFields.AUTH, "O token de atualização foi revogado, faça login");
    }
}
