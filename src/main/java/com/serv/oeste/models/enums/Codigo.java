package com.serv.oeste.models.enums;

import lombok.Getter;

@Getter
public enum Codigo {
    NOMESOBRENOME(1),
    TELEFONECELULAR(2),
    TELEFONEFIXO(3),
    TELEFONES(4),
    CEP(5),
    ENDERECO(6),
    MUNICIPIO(7),
    BAIRRO(8),
    CLIENTE(9);

    private final Integer i;

    Codigo(Integer i) {
        this.i = i;
    }
}
