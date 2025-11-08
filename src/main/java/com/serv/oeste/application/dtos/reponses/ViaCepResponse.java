package com.serv.oeste.application.dtos.reponses;

public record ViaCepResponse(
    String logradouro,
    String bairro,
    String localidade
) { }
