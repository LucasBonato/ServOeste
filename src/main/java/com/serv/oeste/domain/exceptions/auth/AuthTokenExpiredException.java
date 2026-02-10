package com.serv.oeste.domain.exceptions.auth;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.UnauthorizedException;

public class AuthTokenExpiredException extends UnauthorizedException {
    public AuthTokenExpiredException() {
        super(ErrorFields.AUTH, "O token expirou, atualize o token");
    }
}
