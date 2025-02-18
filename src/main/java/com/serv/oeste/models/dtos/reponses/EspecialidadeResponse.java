package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.tecnico.Especialidade;

public record EspecialidadeResponse(
        Integer id,
        String conhecimento
) {
    public EspecialidadeResponse(Especialidade especialidade) {
        this(
                especialidade.getId(),
                especialidade.getConhecimento()
        );
    }
}
