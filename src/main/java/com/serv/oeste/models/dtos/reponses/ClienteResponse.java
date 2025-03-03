package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.cliente.Cliente;

public record ClienteResponse(
        Integer id,
        String nome,
        String telefoneFixo,
        String telefoneCelular,
        String endereco,
        String bairro,
        String municipio
) {
    public ClienteResponse(Cliente cliente) {
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
