package com.serv.oeste.models.dtos.requests;

public record ServicoRequest(
    Integer idCliente,//
    String equipamento,//
    String marca,//
    String filial,//
    String dataAtendimento,
    String horarioPrevisto,
    Integer idTecnico,//
    String descricao//
) { }
