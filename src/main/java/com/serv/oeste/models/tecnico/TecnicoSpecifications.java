package com.serv.oeste.models.tecnico;

import org.springframework.data.jpa.domain.Specification;

public class TecnicoSpecifications {
    public static Specification<Tecnico> hasId(Integer id) {
        return (root, query, cb) -> cb.like(root.get("id").as(String.class), "%" + id + "%");
    }

    public static Specification<Tecnico> hasNome(String nome) {
        return (root, query, cb) -> cb.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<Tecnico> hasSobrenome(String sobrenome) {
        return (root, query, cb) -> cb.like(root.get("sobrenome"), "%" + sobrenome + "%");
    }

    public static Specification<Tecnico> hasSituacao(String situacao) {
        return (root, query, cb) -> cb.like(root.get("situacao"), "%" + situacao + "%");
    }
}
