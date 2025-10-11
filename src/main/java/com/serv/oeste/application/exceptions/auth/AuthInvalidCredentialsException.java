package com.serv.oeste.application.exceptions.auth;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class AuthInvalidCredentialsException extends BaseException {
    public AuthInvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, new ExceptionResponse(Codigo.AUTH, "Credenciais inválidas"));
    }
}
