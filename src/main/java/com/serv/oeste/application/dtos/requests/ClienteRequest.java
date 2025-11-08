package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.entities.client.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "O Nome do cliente não pode ser vazio!")
        @Size(min = 2, message = "O Nome do cliente precisa ter no mínimo {min} caracteres!")
        String nome,

        @NotBlank(message = "Digite Nome e Sobrenome!")
        @Size(min = 2, message = "O Sobrenome do cliente precisa ter no mínimo {min} caracteres!")
        String sobrenome,

        @Pattern(regexp = "\\d{10}|", message = "Telefone fixo inválido")
        String telefoneFixo,

        @Pattern(regexp = "\\d{11}|", message = "Telefone celular inválido")
        String telefoneCelular,

        @NotBlank(message = "O Endereço é obrigatório!")
        String endereco,

        @NotBlank(message = "O Município é obrigatório!")
        String bairro,

        @NotBlank(message = "O Bairro é obrigatório!")
        String municipio
) {
    public Client toClient() {
        return Client.create(
                this.nome,
                this.sobrenome,
                this.telefoneFixo,
                this.telefoneCelular,
                this.endereco,
                this.bairro,
                this.municipio
        );
    }
}