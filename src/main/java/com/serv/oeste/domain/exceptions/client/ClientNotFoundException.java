package com.serv.oeste.domain.exceptions.client;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class ClientNotFoundException extends NotFoundException {
    public ClientNotFoundException() {
        super(ErrorFields.CLIENTE, "Cliente não encontrado!");
    }
}
