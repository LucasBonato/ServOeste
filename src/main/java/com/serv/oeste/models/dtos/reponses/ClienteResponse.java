package com.serv.oeste.models.dtos.reponses;

public record ClienteResponse(
        Integer id,
        String nome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco,
        String bairro,
        String municipio
) { }
