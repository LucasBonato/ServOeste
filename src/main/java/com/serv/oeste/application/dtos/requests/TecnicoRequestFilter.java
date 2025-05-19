package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.valueObjects.TechnicianFilter;

public record TecnicoRequestFilter(
        String id,
        String nome,
        String situacao,
        String equipamento,
        String telefone
) {
    public TechnicianFilter toTechnicianFilter() {
        return new TechnicianFilter(
                this.id,
                this.nome,
                this.situacao,
                this.equipamento,
                this.telefone
        );
    }
}
