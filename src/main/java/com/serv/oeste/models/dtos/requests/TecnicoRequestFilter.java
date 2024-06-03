package com.serv.oeste.models.dtos.requests;

public record TecnicoRequestFilter(
        Integer id,
        String nome,
        String situacao
) { }
