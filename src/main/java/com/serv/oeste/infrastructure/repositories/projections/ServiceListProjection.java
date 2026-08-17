package com.serv.oeste.infrastructure.repositories.projections;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;

import java.time.LocalDate;
import java.util.List;

public record ServiceListProjection(
        Integer id,
        String equipamento,
        String marca,
        String filial,
        String descricao,
        SituacaoServico situacao,
        HorarioPrevisto horarioPrevisto,
        Double valor,
        FormaPagamento formaPagamento,
        Double valorPecas,
        Double valorComissao,
        LocalDate dataPagamentoComissao,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        LocalDate dataInicioGarantia,
        LocalDate dataFimGarantia,
        LocalDate dataAtendimentoPrevisto,
        LocalDate dataAtendimentoEfetiva,
        Integer idCliente,
        String nomeCliente,
        Integer idTecnico,
        String nomeTecnico,
        String sobrenomeTecnico
) {
    public Service toDomain() {
        Client cliente = Client.restore(idCliente, nomeCliente, null, null, null, null, null);
        Technician tecnico = (idTecnico == null)
                ? null
                : Technician.restore(idTecnico, nomeTecnico, sobrenomeTecnico, null, null, null, List.of());

        return Service.restore(
                id,
                equipamento,
                marca,
                filial,
                descricao,
                situacao,
                horarioPrevisto,
                valor,
                formaPagamento,
                valorPecas,
                valorComissao,
                dataPagamentoComissao,
                dataAbertura,
                dataFechamento,
                dataInicioGarantia,
                dataFimGarantia,
                dataAtendimentoPrevisto,
                dataAtendimentoEfetiva,
                cliente,
                tecnico
        );
    }
}