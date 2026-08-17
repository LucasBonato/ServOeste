package com.serv.oeste.infrastructure.repositories.projections;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;

import java.util.List;

public record TechnicianListProjection(
        Integer id,
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        Situacao situacao
) {
    public Technician toDomain() {
        return Technician.restore(
                id,
                nome,
                sobrenome,
                telefoneFixo,
                telefoneCelular,
                situacao,
                List.of()
        );
    }
}