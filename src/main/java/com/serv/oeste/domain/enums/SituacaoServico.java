package com.serv.oeste.domain.enums;

public enum SituacaoServico {
    AGUARDANDO_AGENDAMENTO("Aguardando agendamento"),
    AGUARDANDO_ATENDIMENTO("Aguardando atendimento"),
    AGUARDANDO_APROVACAO("Aguardando aprovação do cliente"),
    AGUARDANDO_CLIENTE_RETIRAR("Aguardando cliente retirar"),
    AGUARDANDO_ORCAMENTO("Aguardando orçamento"),
    CANCELADO("Cancelado"),
    COMPRA("Compra"),
    CORTESIA("Cortesia"),
    GARANTIA("Garantia"),
    NAO_APROVADO("Não aprovado pelo cliente"),
    NAO_RETIRA_3_MESES("Não retira há 3 meses"),
    ORCAMENTO_APROVADO("Orçamento aprovado"),
    RESOLVIDO("Resolvido"),
    SEM_DEFEITO("Sem defeito"),
    ;

    private final String situacao;

    SituacaoServico(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
