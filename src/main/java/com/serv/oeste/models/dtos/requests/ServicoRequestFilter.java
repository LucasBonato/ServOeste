package com.serv.oeste.models.dtos.requests;

import com.serv.oeste.models.enums.SituacaoServico;

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
) { }