package com.serv.oeste.models.viacep;

public record ViaCepDTO(
        String cep,
        String logradouro,
        String cidade,
        String bairro,
        String uf
) { }
