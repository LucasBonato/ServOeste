package com.serv.oeste.infrastructure.specifications;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.infrastructure.entities.service.ServiceEntity;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Date;

public class ServiceSpecifications {
    public static Specification<ServiceEntity> hasCliente(Client cliente) {
        return (root, query, cb) -> cb.equal(root.get("cliente"), cliente);
    }

    public static Specification<ServiceEntity> hasTecnico(Technician tecnico) {
        return (root, query, cb) -> cb.equal(root.get("tecnico"), tecnico);
    }

    public static Specification<ServiceEntity> hasNomeCliente(String clienteNome) {
        return (root, query, cb) -> cb.like(root.join("cliente", JoinType.INNER).get("nome"), "%" + clienteNome + "%");
    }

    public static Specification<ServiceEntity> hasNomeTecnico(String tecnicoNome) {
        return (root, query, cb) -> cb.like(root.join("tecnico", JoinType.INNER).get("nome"), "%" + tecnicoNome + "%");
    }

    public static Specification<ServiceEntity> hasFilial(String filial) {
        return (root, query, cb) -> cb.equal(root.get("filial"), filial);
    }

    public static Specification<ServiceEntity> hasHorarioPrevisto(String periodo) {
        return (root, query, cb) -> cb.equal(root.get("horarioPrevisto"), periodo.toLowerCase().replace("ã", "a"));
    }

    public static Specification<ServiceEntity> hasSituacao(SituacaoServico situacao) {
        return (root, query, cb) -> cb.equal(root.get("situacao"), situacao);
    }

    public static Specification<ServiceEntity> hasGarantia(Boolean garantia) {
        Date hoje = java.sql.Date.valueOf(LocalDate.now());
        return (root, query, cb) -> (garantia)
                ? cb.and(
                    cb.lessThanOrEqualTo(root.get("dataInicioGarantia"), hoje),
                    cb.greaterThanOrEqualTo(root.get("dataFimGarantia"), hoje)
                )
                : cb.or(
                    cb.greaterThan(root.get("dataInicioGarantia"), hoje),
                    cb.lessThan(root.get("dataFimGarantia"), hoje),
                    cb.isNull(root.get("dataInicioGarantia")),
                    cb.isNull(root.get("dataFimGarantia"))
                );
    }

    public static Specification<ServiceEntity> isDataAtendimentoPrevistoBetween(Date dataAtendimentoPrevistoAntes, Date dataAtendimentoPrevistoDepois) {
        return (root, query, cb) -> cb.between(root.get("dataAtendimentoPrevisto"), dataAtendimentoPrevistoAntes, dataAtendimentoPrevistoDepois);
    }

    public static Specification<ServiceEntity> hasDataAtendimentoPrevisto(Date dataAtendimentoPrevisto) {
        return (root, query, cb) -> cb.equal(root.get("dataAtendimentoPrevisto"), dataAtendimentoPrevisto);
    }

    public static Specification<ServiceEntity> hasServicoId(Integer id) {
        return (root, query, cb) -> cb.like(root.get("id").as(String.class), "%" + id + "%");
    }

    public static Specification<ServiceEntity> hasEquipamento(String equipamento) {
        return (root, query, cb) -> cb.like(root.get("equipamento"), "%" + equipamento + "%");
    }

    public static Specification<ServiceEntity> isDataAtendimentoEfetivoBetween(Date dataAtendimentoEfetivoAntes, Date dataAtendimentoEfetivoDepois) {
        return (root, query, cb) -> cb.between(root.get("dataAtendimentoEfetiva"), dataAtendimentoEfetivoAntes, dataAtendimentoEfetivoDepois);
    }

    public static Specification<ServiceEntity> hasDataAtendimentoEfetivo(Date dataAtendimentoEfetivo) {
        return (root, query, cb) -> cb.equal(root.get("dataAtendimentoEfetiva"), dataAtendimentoEfetivo);
    }

    public static Specification<ServiceEntity> isDataAberturaBetween(Date dataAberturaAntes, Date dataAberturaDepois) {
        return (root, query, cb) -> cb.between(root.get("dataAbertura"), dataAberturaAntes, dataAberturaDepois);
    }

    public static Specification<ServiceEntity> hasDataAbertura(Date dataAbertura) {
        return (root, query, cb) -> cb.equal(root.get("dataAbertura"), dataAbertura);
    }
}
