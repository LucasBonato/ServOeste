package com.serv.oeste.application.exceptions.viacep;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ViaCepNetworkException extends BaseException {
    public ViaCepNetworkException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ExceptionResponse(Codigo.CEP.getI(), message));
    }
}
