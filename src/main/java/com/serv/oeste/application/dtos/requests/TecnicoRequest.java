package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record TecnicoRequest(
    String nome,
    String sobrenome,
    String telefoneFixo,
    String telefoneCelular,
    Situacao situacao,
    List<Integer> especialidades_Ids
) {
    public Technician toTechnician() {
        return new Technician(
                this.nome,
                this.sobrenome,
                this.telefoneFixo,
                this.telefoneCelular,
                this.situacao
        );
    }
}
