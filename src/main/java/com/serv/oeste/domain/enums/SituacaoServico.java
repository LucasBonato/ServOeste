package com.serv.oeste.domain.enums;

import java.util.Set;

public enum SituacaoServico {
    AGUARDANDO_AGENDAMENTO("Aguardando agendamento", false) {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of();
        }
    },
    AGUARDANDO_ATENDIMENTO("Aguardando atendimento", false) {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_ORCAMENTO, SEM_DEFEITO, CANCELADO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_AGENDAMENTO);
        }
    },
    AGUARDANDO_ORCAMENTO("Aguardando orçamento", false) {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
    },
    AGUARDANDO_APROVACAO("Aguardando aprovação do cliente", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of(NAO_APROVADO, COMPRA, ORCAMENTO_APROVADO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ORCAMENTO);
        }
    },
    ORCAMENTO_APROVADO("Orçamento aprovado", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    AGUARDANDO_CLIENTE_RETIRAR("Aguardando cliente retirar", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of(GARANTIA, NAO_RETIRA_3_MESES);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(ORCAMENTO_APROVADO);
        }
    },
    GARANTIA("Garantia", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of(CORTESIA, RESOLVIDO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
    },
    NAO_RETIRA_3_MESES("Não retira há 3 meses", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of(RESOLVIDO);
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_CLIENTE_RETIRAR);
        }
    },
    CANCELADO("Cancelado", false) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
    },
    COMPRA("Compra", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    CORTESIA("Cortesia", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(GARANTIA);
        }
    },
    NAO_APROVADO("Não aprovado pelo cliente", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_APROVACAO);
        }
    },
    RESOLVIDO("Resolvido", true) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(GARANTIA, CORTESIA, NAO_RETIRA_3_MESES);
        }
    },
    SEM_DEFEITO("Sem defeito", false) {
        public Set<SituacaoServico> proximos() {
            return Set.of();
        }
        public Set<SituacaoServico> anteriores() {
            return Set.of(AGUARDANDO_ATENDIMENTO);
        }
    },
    ;

    private final String situacao;
    private final boolean exigeFormaPagamento;

    SituacaoServico(String situacao, boolean exigeFormaPagamento) {
        this.situacao = situacao;
        this.exigeFormaPagamento = exigeFormaPagamento;
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
        return exigeFormaPagamento;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
