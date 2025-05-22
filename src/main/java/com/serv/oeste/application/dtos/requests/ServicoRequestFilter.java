package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.domain.valueObjects.ServiceFilter;

import java.util.Date;

public record ServicoRequestFilter(
        Date dataAtendimentoPrevistoAntes,
        Date dataAtendimentoPrevistoDepois,
        Date dataAtendimentoEfetivoAntes,
        Date dataAtendimentoEfetivoDepois,
        Date dataAberturaAntes,
        Date dataAberturaDepois,
        Integer servicoId,
        Integer clienteId,
        Integer tecnicoId,
        String clienteNome,
        String tecnicoNome,
        String equipamento,
        String filial,
        String periodo,
        Boolean garantia,
        SituacaoServico situacao
) {
    public ServiceFilter toServiceFilter() {
        return new ServiceFilter(
                this.dataAtendimentoPrevistoAntes,
                this.dataAtendimentoPrevistoDepois,
                this.dataAtendimentoEfetivoAntes,
                this.dataAtendimentoEfetivoDepois,
                this.dataAberturaAntes,
                this.dataAberturaDepois,
                this.servicoId,
                this.clienteId,
                this.tecnicoId,
                this.clienteNome,
                this.tecnicoNome,
                this.equipamento,
                this.filial,
                this.periodo,
                this.garantia,
                this.situacao
        );
    }
}