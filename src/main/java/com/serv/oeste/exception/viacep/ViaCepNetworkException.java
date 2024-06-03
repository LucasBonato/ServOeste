package com.serv.oeste.exception.viacep;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ViaCepNetworkException extends BaseException {
    public ViaCepNetworkException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ExceptionResponse(Codigo.ENDERECO.ordinal(), message));
    }
}
