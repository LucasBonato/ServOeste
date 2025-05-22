package com.serv.oeste.application.exceptions.address;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class AddressNotValidException extends BaseException {
    public AddressNotValidException(Codigo codigo, String message) {
        super(HttpStatus.BAD_REQUEST, new ExceptionResponse(codigo.getI(), message));
    }
}
