package com.serv.oeste.models.dtos.requests;

import java.util.Date;

public record ServicoRequestFilter(
        Date dataAtendimentoPrevistoAntes,
        Date dataAtendimentoPrevistoDepois,
        Integer clienteId,
        Integer tecnicoId,
        String clienteNome,
        String tecnicoNome,
        String filial,
        String periodo
) { }