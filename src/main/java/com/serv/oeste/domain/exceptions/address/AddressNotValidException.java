package com.serv.oeste.domain.exceptions.address;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.DomainException;

public class AddressNotValidException extends DomainException {
    public AddressNotValidException() {
        super(ErrorFields.CEP, "CEP inexistente!");
    }
}
