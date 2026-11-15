package com.serv.oeste.application.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Periodo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ServicoRequest(
        @NotNull(message = "O cliente é obrigatório")
        Integer idCliente,

        Integer idTecnico,

        @NotBlank(message = "O equipamento é obrigatório")
        String equipamento,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        String filial,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataAtendimento,

        Periodo periodo,

        @Size(max = 500, message = "A descrição pode ter no máximo {max} caracteres")
        String descricao
) {
    public Service toDomain(Client cliente, Technician tecnico) {
        return Service.create(
                equipamento,
                marca,
                filial,
                descricao,
                periodo,
                dataAtendimento,
                cliente,
                tecnico
        );
    }
}
