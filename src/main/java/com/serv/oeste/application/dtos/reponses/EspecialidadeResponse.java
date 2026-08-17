package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.valueObjects.Specialty;

public record EspecialidadeResponse(
        Integer id,
        String conhecimento,
        boolean ativo
) {
    public EspecialidadeResponse(Specialty specialty) {
        this(
                specialty.id(),
                specialty.conhecimento(),
                specialty.isAtivo()
        );
    }
}