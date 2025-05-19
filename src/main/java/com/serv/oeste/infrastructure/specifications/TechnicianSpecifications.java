package com.serv.oeste.infrastructure.specifications;

import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TechnicianSpecifications {
    public static Specification<TechnicianEntity> hasId(String id) {
        return (root, query, cb) -> cb.like(root.get("id").as(String.class), "%" + id + "%");
    }

    public static Specification<TechnicianEntity> hasNomeCompleto(String nomeCompleto) {
        return (Root<TechnicianEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Expression<String> nomeSobrenomeConcat = cb.concat(root.get("nome"), " ");
            nomeSobrenomeConcat = cb.concat(nomeSobrenomeConcat, root.get("sobrenome"));
            return cb.like(nomeSobrenomeConcat, "%" + nomeCompleto + "%");
        };
    }

    public static Specification<TechnicianEntity> hasSituacao(String situacao) {
        return (root, query, cb) -> cb.like(root.get("situacao"), "%" + situacao + "%");
    }

    public static Specification<TechnicianEntity> hasEquipamento(String equipamento) {
        return (root, query, cb) -> cb.equal(root.join("especialidades").get("conhecimento"), equipamento);
    }

    public static Specification<TechnicianEntity> hasTelefone(String telefone) {
        return (root, query, cb) -> cb.or(
                cb.like(root.get("telefoneFixo"), "%" + telefone + "%"),
                cb.like(root.get("telefoneCelular"), "%" + telefone + "%")
        );
    }
}
