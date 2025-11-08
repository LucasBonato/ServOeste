package com.serv.oeste.domain.exceptions.user;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.NotFoundException;
import com.serv.oeste.domain.exceptions.NotValidException;

public class UserNotValidException extends NotValidException {
    public UserNotValidException(String message) {
        super(ErrorFields.USER, message);
    }
}
