package com.serv.oeste.domain.exceptions.service;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.DomainException;

public class ServiceNotFoundException extends DomainException {
    public ServiceNotFoundException() {
        super(ErrorFields.SERVICO, "Serviço não encontrado");
    }
}
