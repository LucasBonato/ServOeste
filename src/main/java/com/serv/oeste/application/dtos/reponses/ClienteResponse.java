package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.client.Client;

public record ClienteResponse(
        Integer id,
        String nome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco,
        String bairro,
        String municipio
) {
    public ClienteResponse(Client cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefoneFixo(),
                cliente.getTelefoneCelular(),
                cliente.getEndereco(),
                cliente.getBairro(),
                cliente.getMunicipio()
        );
    }
}
