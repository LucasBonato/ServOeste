package com.serv.oeste.models.viacep;

public record ViaCepDTO(
        String endereco
) {
    public ViaCepDTO(ViaCep viaCep) {
        this(viaCep.getLogradouro() + "|" + viaCep.getBairro() + "|" + viaCep.getLocalidade());
    }
}
