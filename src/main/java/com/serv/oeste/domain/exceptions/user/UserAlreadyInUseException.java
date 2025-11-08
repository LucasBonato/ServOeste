package com.serv.oeste.domain.exceptions.user;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;

public class UserAlreadyInUseException extends UserNotValidException {
    public UserAlreadyInUseException() {
        super("Nome de usuário já está em uso");
    }
}
