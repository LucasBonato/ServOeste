package com.serv.oeste.models.servico;

import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.tecnico.Tecnico;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ServicoSpecifications {
    public static Specification<Servico> hasCliente(Cliente cliente) {
        return (root, query, cb) -> cb.equal(root.get("cliente"), cliente);
    }

    public static Specification<Servico> hasTecnico(Tecnico tecnico) {
        return (root, query, cb) -> cb.equal(root.get("tecnico"), tecnico);
    }

    public static Specification<Servico> hasNomeCliente(String clienteNome) {
        return (root, query, cb) -> cb.like(root.join("cliente", JoinType.INNER).get("nome"), "%" + clienteNome + "%");
    }

    public static Specification<Servico> hasNomeTecnico(String tecnicoNome) {
        return (root, query, cb) -> cb.like(root.join("tecnico", JoinType.INNER).get("nome"), "%" + tecnicoNome + "%");
    }

    public static Specification<Servico> hasFilial(String filial) {
        return (root, query, cb) -> cb.equal(root.get("filial"), filial);
    }

    public static Specification<Servico> hasHorarioPrevisto(String periodo) {
        return (root, query, cb) -> cb.equal(root.get("horarioPrevisto"), periodo);
    }

    public static Specification<Servico> isDataAtendimentoPrevistoBetween(Date dataAtendimentoPrevistoAntes, Date dataAtendimentoPrevistoDepois) {
        return (root, query, cb) -> cb.between(root.get("dataAtendimentoPrevisto"), dataAtendimentoPrevistoAntes, dataAtendimentoPrevistoDepois);
    }

    public static Specification<Servico> hasDataAtendimentoPrevisto(Date dataAtendimentoPrevisto) {
        return (root, query, cb) -> cb.equal(root.get("dataAtendimentoPrevisto"), dataAtendimentoPrevisto);
    }
}
