package com.serv.oeste.application.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ServicoUpdateRequest(
        @NotNull(message = "O técnico é obrigatório")
        Integer idTecnico,

        @NotNull(message = "O cliente é obrigatório")
        Integer idCliente,

        @NotBlank(message = "O equipamento é obrigatório")
        String equipamento,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "A filial é obrigatória")
        String filial,

        @Size(max = 500, message = "A descrição pode ter no máximo {max} caracteres")
        String descricao,

        @NotNull(message = "A situação do serviço é obrigatória")
        SituacaoServico situacao,

        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        @NotBlank(message = "O horário previsto é obrigatório")
        HorarioPrevisto horarioPrevisto,

        @PositiveOrZero(message = "O valor deve ser maior ou igual a zero")
        Double valor,

        @PositiveOrZero(message = "O valor da comissão deve ser maior ou igual a zero")
        Double valorComissao,

        @PositiveOrZero(message = "O valor das peças deve ser maior ou igual a zero")
        Double valorPecas,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataFechamento,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataInicioGarantia,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataFimGarantia,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataAtendimentoPrevisto,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataAtendimentoEfetiva,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataPagamentoComissao
) { }