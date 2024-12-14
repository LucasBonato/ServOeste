package com.serv.oeste.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Situacao {
    ATIVO("ativo"),
    LICENCA("licença"),
    DESATIVADO("desativado");

    final private String situacao;
}
