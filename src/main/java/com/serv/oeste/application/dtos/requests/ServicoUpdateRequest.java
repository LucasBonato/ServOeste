package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.SituacaoServico;

public record ServicoUpdateRequest(
        Integer idTecnico,
        Integer idCliente,
        String equipamento,
        String marca,
        String filial,
        String descricao,
        SituacaoServico situacao,
        FormaPagamento formaPagamento,
        String horarioPrevisto,
        Double valor,
        Double valorComissao,
        Double valorPecas,
        String dataFechamento,
        String dataInicioGarantia,
        String dataFimGarantia,
        String dataAtendimentoPrevisto,
        String dataAtendimentoEfetiva,
        String dataPagamentoComissao
)
{ }