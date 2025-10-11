package com.serv.oeste.domain.exceptions.user;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(ErrorFields.USER, "Usuário não encontrado");
    }
}
