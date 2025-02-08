package com.serv.oeste.models.dtos.requests;

import com.serv.oeste.models.enums.FormaPagamento;
import com.serv.oeste.models.enums.SituacaoServico;

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