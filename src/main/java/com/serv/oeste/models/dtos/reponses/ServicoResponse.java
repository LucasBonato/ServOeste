package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.enums.SituacaoServico;

import java.util.Date;

public record ServicoResponse (
    Integer id,
    Integer idCliente,
    Integer idTecnico,
    String equipamento,
    String filial,
    String horarioPrevisto,
    String marca,
    SituacaoServico situacao,
    Date dataAtendimentoPrevisto
) {}
