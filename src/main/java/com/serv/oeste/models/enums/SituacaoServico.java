package com.serv.oeste.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoServico {
    AGUARDANDO_AGENDAMENTO("Aguardando agendamento"),
    AGUARDANDO_ATENDIMENTO("Aguardando atendimento"),
    AGUARDANDO_APROVACAO("Aguardando aprovação"),
    ORCAMENTO_APROVADO("Orçamento aprovado"),
    SEM_DEFEITO("Sem defeito"),
    NAO_APROVADO("Não aprovado"),
    CANCELADO("Cancelado"),
    AGUARDANDO_CLIENTE_RETIRAR("Aguardando cliente retirar"),
    NAO_RETIRA_3_MESES("Não retira há 3 meses"),
    CORTESIA("Cortesia"),
    GARANTIA("Garantia");

    private final String situacao;
}
