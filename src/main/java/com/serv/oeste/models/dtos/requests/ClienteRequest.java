package com.serv.oeste.models.dtos.requests;

public record ClienteRequest(
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco,
        String bairro,
        String municipio
) { }
