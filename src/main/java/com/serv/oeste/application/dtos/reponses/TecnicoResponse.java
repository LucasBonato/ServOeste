package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;

public record TecnicoResponse(
        Integer id,
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        Situacao situacao
) {
    public TecnicoResponse(Technician technician) {
        this(
                technician.getId(),
                technician.getNome(),
                technician.getSobrenome(),
                technician.getTelefoneFixo(),
                technician.getTelefoneCelular(),
                technician.getSituacao()
        );
    }
}
