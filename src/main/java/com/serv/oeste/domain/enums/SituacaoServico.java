package com.serv.oeste.domain.enums;

import java.util.Set;

public enum SituacaoServico {
    AGUARDANDO_AGENDAMENTO("Aguardando agendamento") {
        public Set<SituacaoServico> anteriores() { return Set.of(); }
        public Set<SituacaoServico> proximos() { return Set.of(AGUARDANDO_ATENDIMENTO); }
    },
    AGUARDANDO_ATENDIMENTO("Aguardando atendimento") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_AGENDAMENTO); }
        public Set<SituacaoServico> proximos() { return Set.of(SEM_DEFEITO, CANCELADO, AGUARDANDO_ORCAMENTO); }
    },
    SEM_DEFEITO("Sem defeito") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_ATENDIMENTO); }
        public Set<SituacaoServico> proximos() { return Set.of(); }
    },
    CANCELADO("Cancelado") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_ATENDIMENTO); }
        public Set<SituacaoServico> proximos() { return Set.of(); }
    },
    AGUARDANDO_ORCAMENTO("Aguardando orçamento") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_ATENDIMENTO); }
        public Set<SituacaoServico> proximos() { return Set.of(AGUARDANDO_APROVACAO); }
    },
    AGUARDANDO_APROVACAO("Aguardando aprovação do cliente") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_ORCAMENTO); }
        public Set<SituacaoServico> proximos() { return Set.of(NAO_APROVADO, COMPRA, ORCAMENTO_APROVADO); }
    },
    NAO_APROVADO("Não aprovado pelo cliente") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_APROVACAO); }
        public Set<SituacaoServico> proximos() { return Set.of(); }
    },
    COMPRA("Compra") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_APROVACAO); }
        public Set<SituacaoServico> proximos() { return Set.of(); }
    },
    ORCAMENTO_APROVADO("Orçamento aprovado") {
        public Set<SituacaoServico> anteriores() { return Set.of(AGUARDANDO_APROVACAO); }
        public Set<SituacaoServico> proximos() { return Set.of(RESOLVIDO); }
    },
    RESOLVIDO("Resolvido") {
        public Set<SituacaoServico> anteriores() { return Set.of(ORCAMENTO_APROVADO, GARANTIA); }
        public Set<SituacaoServico> proximos() { return Set.of(GARANTIA); }
    },
    GARANTIA("Garantia") {
        public Set<SituacaoServico> anteriores() { return Set.of(RESOLVIDO); }
        public Set<SituacaoServico> proximos() { return Set.of(RESOLVIDO); }
    },
    ;

    private final String situacao;

    SituacaoServico(String situacao) {
        this.situacao = situacao;
    }

    public abstract Set<SituacaoServico> proximos();
    public abstract Set<SituacaoServico> anteriores();

    public boolean podeAvancarPara(SituacaoServico destino) {
        return proximos().contains(destino);
    }

    public boolean podeRetornarPara(SituacaoServico destino) {
        return anteriores().contains(destino);
    }

    public boolean isInicial() {
        return this == AGUARDANDO_AGENDAMENTO
                || this == AGUARDANDO_ATENDIMENTO;
    }

    public boolean exigeEquipamento() {
        return this == AGUARDANDO_AGENDAMENTO
                || this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == CANCELADO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeMarca() {
        return this == AGUARDANDO_AGENDAMENTO
                || this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == CANCELADO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeTecnico() {
        return this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigePeriodo() {
        return this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeFilial() {
        return this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == CANCELADO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDescricao() {
        return this == AGUARDANDO_AGENDAMENTO
                || this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == NAO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataAtendimentoPrevisto() {
        return this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == COMPRA
                || this == NAO_APROVADO
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataAtendimentoEfetivo() {
        return this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == COMPRA
                || this == NAO_APROVADO
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeValorServico() {
        return this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeValorPecas() {
        return this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeValorComissao() {
        return this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == ORCAMENTO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeFormaPagamento() {
        return this == COMPRA
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataPagamentoComissao() {
        return this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataFechamento() {
        return this == SEM_DEFEITO
                || this == COMPRA
                || this == NAO_APROVADO
                || this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataInicioGarantia() {
        return this == RESOLVIDO
                || this == GARANTIA;
    }

    public boolean exigeDataFimGarantia() {
        return this == RESOLVIDO
                || this == GARANTIA;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
