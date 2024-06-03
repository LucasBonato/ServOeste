package com.serv.oeste.models.cliente;

import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecifications {
    public static Specification<Cliente> hasNome(String nome) {
        return (root, query, cb) -> cb.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<Cliente> hasTelefoneCelular(String telefoneCelular) {
        return (root, query, cb) -> cb.like(root.get("telefoneCelular"), "%" + telefoneCelular + "%");
    }

    public static Specification<Cliente> hasTelefoneFixo(String telefoneFixo) {
        return (root, query, cb) -> cb.like(root.get("telefoneFixo"), "%" + telefoneFixo + "%");
    }

    public static Specification<Cliente> hasEndereco(String endereco) {
        return (root, query, cb) -> cb.like(root.get("endereco"), "%" + endereco + "%");
    }
}
