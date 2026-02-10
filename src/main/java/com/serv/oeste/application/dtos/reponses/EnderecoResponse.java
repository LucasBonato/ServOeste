package com.serv.oeste.application.dtos.reponses;

public record EnderecoResponse(
        String logradouro,
        String bairro,
        String municipio
) {
  public EnderecoResponse(ViaCepResponse viaCep) {
      this(
              viaCep.logradouro(),
              viaCep.bairro(),
              viaCep.localidade()
      );
  }
}
