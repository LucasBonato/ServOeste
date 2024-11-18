package com.serv.oeste.models.servico;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ServicoSpecifications {
    public static Specification<Servico> hasClienteId(Integer clienteId) {
        return (root, query, cb) -> cb.equal(root.get("id_cliente"), clienteId);
    }

    public static Specification<Servico> hasTecnicoId(Integer tecnicoId) {
        return (root, query, cb) -> cb.equal(root.get("id_tecnico"), tecnicoId);
    }

    public static Specification<Servico> hasFilial(String filial) {
        return (root, query, cb) -> cb.equal(root.get("filial"), filial);
    }

    public static Specification<Servico> hasHorarioPrevisto(String periodo) {
        return (root, query, cb) -> cb.equal(root.get("horario_previsto"), periodo);
    }

    public static Specification<Servico> isDataAtendimentoPrevistoBetween(Date dataAtendimentoPrevistoAntes, Date dataAtendimentoPrevistoDepois) {
        return (root, query, cb) -> cb.between(root.get("data_atendimento_previsto"), dataAtendimentoPrevistoAntes, dataAtendimentoPrevistoDepois);
    }

    public static Specification<Servico> hasDataAtendimentoPrevisto(Date dataAtendimentoPrevisto) {
        return (root, query, cb) -> cb.equal(root.get("data_atendimento_previsto"), dataAtendimentoPrevisto);
    }
}
