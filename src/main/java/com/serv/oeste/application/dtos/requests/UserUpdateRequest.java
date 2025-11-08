package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(@NotNull Integer id, String username, String password, Roles role) {
    public User toDomain() {
        return User.restore(id, username, password, role);
    }
}
