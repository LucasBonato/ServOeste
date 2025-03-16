package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.viacep.ViaCep;

public record EnderecoResponse(
        String logradouro,
        String bairro,
        String municipio
) {
  public EnderecoResponse(ViaCep viaCep) {
      this(viaCep.getLogradouro(), viaCep.getBairro(), viaCep.getLocalidade());
  }
}
