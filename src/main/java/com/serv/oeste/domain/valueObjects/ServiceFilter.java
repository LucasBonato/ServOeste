package com.serv.oeste.domain.valueObjects;

import com.serv.oeste.domain.enums.SituacaoServico;

import java.util.Date;

public record ServiceFilter(
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
        String marca,
        String filial,
        String periodo,
        Boolean garantia,
        SituacaoServico situacao
) { }