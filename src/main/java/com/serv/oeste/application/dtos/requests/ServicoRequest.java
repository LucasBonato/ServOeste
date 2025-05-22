package com.serv.oeste.application.dtos.requests;

public record ServicoRequest(
        Integer idCliente,
        Integer idTecnico,
        String equipamento,
        String marca,
        String filial,
        String dataAtendimento,
        String horarioPrevisto,
        String descricao
) { }
