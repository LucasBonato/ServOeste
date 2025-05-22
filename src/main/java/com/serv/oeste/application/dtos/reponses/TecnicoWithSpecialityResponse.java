package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;

import java.util.List;
import java.util.stream.Collectors;

public record TecnicoWithSpecialityResponse(
        Integer id,
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        Situacao situacao,
        List<EspecialidadeResponse> especialidades
) {
    public TecnicoWithSpecialityResponse(Technician technician) {
        this(
                technician.getId(),
                technician.getNome(),
                technician.getSobrenome(),
                technician.getTelefoneFixo(),
                technician.getTelefoneCelular(),
                technician.getSituacao(),
                technician.getEspecialidades().stream()
                        .map(EspecialidadeResponse::new)
                        .collect(Collectors.toList())
        );
    }
}
