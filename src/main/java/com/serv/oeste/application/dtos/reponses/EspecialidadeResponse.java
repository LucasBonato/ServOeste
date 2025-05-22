package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.specialty.Specialty;

public record EspecialidadeResponse(
        Integer id,
        String conhecimento
) {
    public EspecialidadeResponse(Specialty specialty) {
        this(
                specialty.getId(),
                specialty.getConhecimento()
        );
    }
}
