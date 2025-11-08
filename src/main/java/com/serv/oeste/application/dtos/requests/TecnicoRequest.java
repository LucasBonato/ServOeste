package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TecnicoRequest(
        @NotBlank(message = "O Nome do técnico não pode ser vazio!")
        @Size(min = 2, message = "O Nome do técnico precisa ter no mínimo {min} caracteres!")
        String nome,

        @NotBlank(message = "Digite Nome e Sobrenome!")
        @Size(min = 2, message = "O Sobrenome do técnico precisa ter no mínimo {minsd} caracteres!")
        String sobrenome,

        @Pattern(regexp = "\\d{10}|", message = "Telefone fixo inválido")
        String telefoneFixo,

        @Pattern(regexp = "\\d{11}|", message = "Telefone celular inválido")
        String telefoneCelular,

        Situacao situacao,

        List<Integer> especialidades_Ids
) {
    public Technician toTechnician(List<Specialty> especialidades) {
        return Technician.create(
                this.nome,
                this.sobrenome,
                this.telefoneFixo,
                this.telefoneCelular,
                especialidades
        );
    }
}
