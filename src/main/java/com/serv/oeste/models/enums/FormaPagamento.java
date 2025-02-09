package com.serv.oeste.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormaPagamento {
    DEBITO("Débito"),
    DINHEIRO("Dinheiro"),
    CREDITO("Crédito"),
    PIX("Pix"),
    BOLETO("Boleto"),
    ;

    private final String formaPagamento;
}
