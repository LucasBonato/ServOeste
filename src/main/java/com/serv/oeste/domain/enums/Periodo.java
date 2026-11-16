package com.serv.oeste.domain.enums;

public enum Periodo {
    MANHA("Manhã"),
    TARDE("Tarde");

    private final String value;

    Periodo(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isManha() { return this == MANHA; }
    public boolean isTarde() { return this == TARDE; }
}
