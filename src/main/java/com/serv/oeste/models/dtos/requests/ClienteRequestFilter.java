package com.serv.oeste.models.dtos.requests;

public record ClienteRequestFilter(
        String nome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco
) { }
