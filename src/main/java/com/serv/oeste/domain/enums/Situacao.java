package com.serv.oeste.domain.enums;

public enum Situacao {
    ATIVO("ativo"),
    LICENCA("licença"),
    DESATIVADO("desativado");

    final private String situacao;

    Situacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
