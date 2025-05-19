package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.entities.client.Client;

public record ClienteRequest(
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco,
        String bairro,
        String municipio
) {
    public Client toClient() {
        return new Client(
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