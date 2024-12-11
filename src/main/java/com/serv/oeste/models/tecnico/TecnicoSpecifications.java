package com.serv.oeste.models.tecnico;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TecnicoSpecifications {
    public static Specification<Tecnico> hasId(String id) {
        return (root, query, cb) -> cb.like(root.get("id").as(String.class), "%" + id + "%");
    }

    public static Specification<Tecnico> hasNomeCompleto(String nomeCompleto) {
        return (Root<Tecnico> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Expression<String> nomeSobrenomeConcat = cb.concat(root.get("nome"), " ");
            nomeSobrenomeConcat = cb.concat(nomeSobrenomeConcat, root.get("sobrenome"));
            return cb.like(nomeSobrenomeConcat, "%" + nomeCompleto + "%");
        };
    }

    public static Specification<Tecnico> hasSituacao(String situacao) {
        return (root, query, cb) -> cb.like(root.get("situacao"), "%" + situacao + "%");
    }

    public static Specification<Tecnico> hasEquipamento(String equipamento) {
        return (root, query, cb) -> cb.equal(root.join("especialidades").get("conhecimento"), equipamento);
    }
}
