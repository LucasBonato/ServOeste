package com.serv.oeste.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormaPagamento {
    DEBITO("Débito"),
    CREDITO("Crédito"),
    PIX("PIX"),
    BOLETO("Boleto"),
    ;

    private final String formaPagamento;
}
