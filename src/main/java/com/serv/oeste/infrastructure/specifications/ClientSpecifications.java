package com.serv.oeste.infrastructure.specifications;

import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecifications {
    public static Specification<ClientEntity> hasNome(String nome) {
        return (root, query, cb) -> cb.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<ClientEntity> hasTelefone(String telefone) {
        return (root, query, cb) -> cb.or(
                cb.like(root.get("telefoneFixo"), "%" + telefone + "%"),
                cb.like(root.get("telefoneCelular"), "%" + telefone + "%")
        );
    }

    public static Specification<ClientEntity> hasEndereco(String endereco) {
        return (root, query, cb) -> cb.like(root.get("endereco"), "%" + endereco + "%");
    }
}
