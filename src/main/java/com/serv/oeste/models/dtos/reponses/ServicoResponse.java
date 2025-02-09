package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.enums.FormaPagamento;
import com.serv.oeste.models.enums.SituacaoServico;

import java.util.Date;

public record ServicoResponse (
    Integer id,
    Integer idCliente,
    Integer idTecnico,
    String nomeCliente,
    String nomeTecnico,
    String equipamento,
    String filial,
    String horarioPrevisto,
    String marca,
    String descricao,
    String formaPagamento,
    SituacaoServico situacao,
    Double valor,
    Double valorComissao,
    Double valorPecas,
    Date dataAtendimentoPrevisto,
    Date dataFechamento,
    Date dataInicioGarantia,
    Date dataFimGarantia,
    Date dataAtendimentoEfetiva,
    Date dataPagamentoComissao
) {}
