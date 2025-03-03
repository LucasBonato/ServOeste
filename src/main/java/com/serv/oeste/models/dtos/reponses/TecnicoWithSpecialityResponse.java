package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.enums.Situacao;
import com.serv.oeste.models.tecnico.Tecnico;

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
    public TecnicoWithSpecialityResponse(Tecnico tecnico) {
        this(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getSobrenome(),
                tecnico.getTelefoneFixo(),
                tecnico.getTelefoneCelular(),
                tecnico.getSituacao(),
                tecnico.getEspecialidades().stream().map(EspecialidadeResponse::new).collect(Collectors.toList())
        );
    }
}
