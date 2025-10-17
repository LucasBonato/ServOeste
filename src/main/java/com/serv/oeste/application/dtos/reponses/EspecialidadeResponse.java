package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.valueObjects.Specialty;

public record EspecialidadeResponse(
        Integer id,
        String conhecimento
) {
    public EspecialidadeResponse(Specialty specialty) {
        this(
                specialty.id(),
                specialty.conhecimento()
        );
    }
}
