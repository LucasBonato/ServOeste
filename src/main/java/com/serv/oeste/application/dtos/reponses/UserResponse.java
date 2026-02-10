package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;

public record UserResponse(
        Integer id,
        String username,
        Roles role
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
