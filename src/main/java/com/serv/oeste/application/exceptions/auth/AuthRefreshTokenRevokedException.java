package com.serv.oeste.application.exceptions.auth;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class AuthRefreshTokenRevokedException extends BaseException {
    public AuthRefreshTokenRevokedException() {
        super(HttpStatus.FORBIDDEN, new ExceptionResponse(Codigo.AUTH, "The Refresh token has been revoked, please login"));
    }
}
