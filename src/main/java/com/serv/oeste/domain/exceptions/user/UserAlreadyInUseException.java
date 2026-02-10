package com.serv.oeste.domain.exceptions.user;

public class UserAlreadyInUseException extends UserNotValidException {
    public UserAlreadyInUseException() {
        super("Nome de usuário já está em uso");
    }
}
