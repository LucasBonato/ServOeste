package com.serv.oeste.domain.enums;

import java.util.Set;

public enum SituacaoServico {
    AGUARDANDO_AGENDAMENTO("Aguardando agendamento") {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of();
        }
    },
    AGUARDANDO_ATENDIMENTO("Aguardando atendimento") {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_ORCAMENTO, SEM_DEFEITO, CANCELADO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_AGENDAMENTO);
        }
    },
    AGUARDANDO_ORCAMENTO("Aguardando orçamento") {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
    },
    AGUARDANDO_APROVACAO("Aguardando aprovação do cliente") {
        public Set<SituacaoServico> proximos() {
            return Set.of(NAO_APROVADO, COMPRA, ORCAMENTO_APROVADO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ORCAMENTO);
        }
    },
    ORCAMENTO_APROVADO("Orçamento aprovado") {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    AGUARDANDO_CLIENTE_RETIRAR("Aguardando cliente retirar") {
        public Set<SituacaoServico> proximos() {
            return Set.of(GARANTIA, NAO_RETIRA_3_MESES);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(ORCAMENTO_APROVADO);
        }
    },
    GARANTIA("Garantia") {
        public Set<SituacaoServico> proximos() {
            return Set.of(CORTESIA, RESOLVIDO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
    },
    NAO_RETIRA_3_MESES("Não retira há 3 meses") {
        public Set<SituacaoServico> proximos() {
            return Set.of(RESOLVIDO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
    },
    CANCELADO("Cancelado") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
    },
    COMPRA("Compra") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    CORTESIA("Cortesia") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(GARANTIA);
        }
    },
    NAO_APROVADO("Não aprovado pelo cliente") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    RESOLVIDO("Resolvido") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(GARANTIA, CORTESIA, NAO_RETIRA_3_MESES);
        }
    },
    SEM_DEFEITO("Sem defeito") {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
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

    public static boolean isInicial(SituacaoServico situacao) {
        return situacao == SituacaoServico.AGUARDANDO_AGENDAMENTO ||
                situacao == SituacaoServico.AGUARDANDO_ATENDIMENTO;
    }

    public boolean exigeFormaPagamento() {
        return this == AGUARDANDO_APROVACAO
                || this == ORCAMENTO_APROVADO
                || this == AGUARDANDO_CLIENTE_RETIRAR
                || this == GARANTIA
                || this == NAO_RETIRA_3_MESES
                || this == COMPRA
                || this == CORTESIA
                || this == NAO_APROVADO
                || this == RESOLVIDO;
    }

    public boolean exigeValorServico() {
        return this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == AGUARDANDO_CLIENTE_RETIRAR
                || this == NAO_RETIRA_3_MESES
                || this == RESOLVIDO
                || this == CORTESIA
                || this == GARANTIA;
    }

    public boolean exigeDataFechamento() {
        return this == RESOLVIDO
                || this == CORTESIA
                || this == GARANTIA;
    }

    public boolean exigePagamentoComissao() {
        return this == SEM_DEFEITO
                || this == CANCELADO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == RESOLVIDO;
    }

    public boolean exigeFimGarantia() {
        return this == RESOLVIDO
                || this == CORTESIA
                || this == GARANTIA;
    }

    public boolean exigeAtendimentoPrevisto() {
        return this == AGUARDANDO_ATENDIMENTO
                || this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == AGUARDANDO_CLIENTE_RETIRAR
                || this == NAO_RETIRA_3_MESES
                || this == RESOLVIDO
                || this == CORTESIA
                || this == GARANTIA;
    }

    public boolean exigeAtendimentoEfetivo() {
        return this == SEM_DEFEITO
                || this == AGUARDANDO_ORCAMENTO
                || this == AGUARDANDO_APROVACAO
                || this == NAO_APROVADO
                || this == COMPRA
                || this == ORCAMENTO_APROVADO
                || this == AGUARDANDO_CLIENTE_RETIRAR
                || this == NAO_RETIRA_3_MESES
                || this == RESOLVIDO
                || this == CORTESIA
                || this == GARANTIA;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
