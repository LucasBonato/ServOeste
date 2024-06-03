package com.serv.oeste.models.viacep;

import lombok.Data;

@Data
public class ViaCep {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
