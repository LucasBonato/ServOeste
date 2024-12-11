package com.serv.oeste.models.dtos.requests;

public record TecnicoRequestFilter(
        String id,
        String nome,
        String situacao,
        String equipamento
) { }
