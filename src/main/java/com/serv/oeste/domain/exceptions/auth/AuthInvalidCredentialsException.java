package com.serv.oeste.domain.exceptions.auth;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.UnauthorizedException;

public class AuthInvalidCredentialsException extends UnauthorizedException {
    public AuthInvalidCredentialsException() {
        super(ErrorFields.AUTH, "Credenciais inválidas");
    }
}
