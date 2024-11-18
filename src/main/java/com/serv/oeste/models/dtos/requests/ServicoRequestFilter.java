package com.serv.oeste.models.dtos.requests;

import java.util.Date;

public record ServicoRequestFilter(
        Date dataAtendimentoPrevistoAntes,
        Date dataAtendimentoPrevistoDepois,
        Integer clienteId,
        Integer tecnicoId,
        String filial,
        String periodo
) { }