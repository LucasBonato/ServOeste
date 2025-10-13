package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ServicoResponse (
    Integer id,
    Integer idCliente,
    Integer idTecnico,
    String nomeCliente,
    String nomeTecnico,
    String equipamento,
    String filial,
    HorarioPrevisto horarioPrevisto,
    String marca,
    String descricao,
    String formaPagamento,
    SituacaoServico situacao,
    Boolean garantia,
    Double valor,
    Double valorComissao,
    Double valorPecas,
    LocalDate dataAtendimentoPrevisto,
    LocalDate dataFechamento,
    LocalDate dataInicioGarantia,
    LocalDate dataFimGarantia,
    LocalDate dataAtendimentoEfetiva,
    LocalDate dataPagamentoComissao
) {
    public ServicoResponse(Service servico) {
        this(
                servico.getId(),
                servico.getCliente().getId(),
                (servico.getTecnico() != null) ? servico.getTecnico().getId() : null,
                servico.getCliente().getNome(),
                (servico.getTecnico() != null) ? servico.getTecnico().getNome() + " " + servico.getTecnico().getSobrenome() : null,
                servico.getEquipamento(),
                servico.getFilial(),
                servico.getHorarioPrevisto(),
                servico.getMarca(),
                servico.getDescricao(),
                (servico.getFormaPagamento() != null) ? servico.getFormaPagamento().getFormaPagamento() : null,
                servico.getSituacao(),
                servico.getHasGarantia(),
                servico.getValor(),
                servico.getValorComissao(),
                servico.getValorPecas(),
                servico.getDataAtendimentoPrevisto(),
                servico.getDataFechamento(),
                servico.getDataInicioGarantia(),
                servico.getDataFimGarantia(),
                servico.getDataAtendimentoEfetiva(),
                servico.getDataPagamentoComissao()
        );
    }
}
