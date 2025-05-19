package com.serv.oeste.domain.enums;

public enum FormaPagamento {
    DEBITO("Débito"),
    DINHEIRO("Dinheiro"),
    CREDITO("Crédito"),
    PIX("Pix"),
    BOLETO("Boleto"),
    ;

    private final String formaPagamento;

    FormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getFormaPagamento() {
        return this.formaPagamento;
    }
}
