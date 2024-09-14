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
    CLIENTE(9),
    TECNICO(10),
    EQUIPAMENTO(11),
    MARCA(12),
    DESCRICAO(13),
    FILIAL(14),
    HORARIO(15),
    DATA(16);

    private final Integer i;

    Codigo(Integer i) {
        this.i = i;
    }
}
