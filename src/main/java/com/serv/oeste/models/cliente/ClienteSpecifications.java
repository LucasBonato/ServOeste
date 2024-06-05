package com.serv.oeste.models.cliente;

import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecifications {
    public static Specification<Cliente> hasNome(String nome) {
        return (root, query, cb) -> cb.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<Cliente> hasTelefone(String telefone) {
        return (root, query, cb) -> cb.or(
                cb.like(root.get("telefoneFixo"), "%" + telefone + "%"),
                cb.like(root.get("telefoneCelular"), "%" + telefone + "%")
        );
    }

    public static Specification<Cliente> hasEndereco(String endereco) {
        return (root, query, cb) -> cb.like(root.get("endereco"), "%" + endereco + "%");
    }
}
