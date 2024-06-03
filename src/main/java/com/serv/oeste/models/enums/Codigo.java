package com.serv.oeste.models.enums;

import lombok.Getter;

@Getter
public enum Codigo {
    NOMESOBRENOME(1),
    TELEFONECELULAR(2),
    TELEFONEFIXO(3),
    TELEFONES(4),
    ENDERECO(5),
    MUNICIPIO(6),
    BAIRRO(7),
    CLIENTE(8);

    Codigo(Integer i) {}
}
