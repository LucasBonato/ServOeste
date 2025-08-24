package com.serv.oeste.domain.enums;

public enum Roles {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE"),
    TECHNICIAN("TECHNICIAN");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public String getRoleWithPrefix() {
        return String.format("ROLE_%s", this.role);
    }
}
