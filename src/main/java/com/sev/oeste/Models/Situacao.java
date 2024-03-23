package com.sev.oeste.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Situacao {
    ATIVO("ativo"),
    LICENCA("licença"),
    DESATIVADO("desativado");

    private String situacao;
}
