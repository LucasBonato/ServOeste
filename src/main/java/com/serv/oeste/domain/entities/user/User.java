package com.serv.oeste.domain.entities.user;

import com.serv.oeste.domain.enums.Roles;

public class User {
    private Integer id;
    private String username;
    private String passwordHash;
    private Roles role;

    public User(String username, String passwordHash, Roles role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    private User(Integer id, String username, String passwordHash, Roles role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public void update(String username, String passwordHash, Roles role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public static User restore(Integer id, String username, String passwordHash, Roles role) {
        return new User (id, username, passwordHash, role);
    }

    public Integer getId() {
        return id;
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