package com.serv.oeste.domain.entities.user;

import com.serv.oeste.domain.enums.Roles;

public class User {
    private final String username;
    private final String passwordHash;
    private final Roles role;

    public User(String username, String passwordHash, Roles role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Roles getRole() {
        return role;
    }
}