package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.viacep.ViaCep;

public record EnderecoResponse(
        String logradouro,
        String bairro,
        String municipio
) {
  public EnderecoResponse(ViaCep viaCep) {
      this(viaCep.getLogradouro(), viaCep.getBairro(), viaCep.getLocalidade());
  }
}
