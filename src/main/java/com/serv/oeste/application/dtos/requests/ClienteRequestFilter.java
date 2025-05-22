package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.valueObjects.ClientFilter;

public record ClienteRequestFilter(
        String nome,
        String telefone,
        String endereco
) {
    public ClientFilter toClientFilter() {
        return new ClientFilter(this.nome, this.telefone, this.endereco);
    }
}
