package com.serv.oeste.models.dtos.requests;

public record ClienteRequestFilter(
        String nome,
        String telefone,
        String endereco
) { }
