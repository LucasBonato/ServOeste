package com.serv.oeste.exception.viacep;

import com.serv.oeste.exception.BaseException;
import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ViaCepServerDownException extends BaseException {
    public ViaCepServerDownException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, new ExceptionResponse(Codigo.ENDERECO.ordinal(), "Servidor da ViaCep caiu."));
    }
}
