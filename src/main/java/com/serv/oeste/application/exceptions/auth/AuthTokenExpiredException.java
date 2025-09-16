package com.serv.oeste.application.exceptions.auth;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class AuthTokenExpiredException extends BaseException {
    public AuthTokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, new ExceptionResponse(Codigo.AUTH, "O token expirou, atualize a página"));
    }
}
