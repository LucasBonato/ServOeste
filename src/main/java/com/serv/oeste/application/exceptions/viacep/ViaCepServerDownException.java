package com.serv.oeste.application.exceptions.viacep;

import com.serv.oeste.application.exceptions.BaseException;
import com.serv.oeste.application.exceptions.ExceptionResponse;
import com.serv.oeste.domain.enums.Codigo;
import org.springframework.http.HttpStatus;

public class ViaCepServerDownException extends BaseException {
    public ViaCepServerDownException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, new ExceptionResponse(Codigo.ENDERECO.getI(), "Servidor da ViaCep caiu."));
    }
}
