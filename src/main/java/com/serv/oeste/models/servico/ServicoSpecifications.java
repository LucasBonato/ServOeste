package com.serv.oeste.models.servico;

import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.tecnico.Tecnico;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ServicoSpecifications {
    public static Specification<Servico> hasCliente(Cliente cliente) {
        return (root, query, cb) -> cb.equal(root.get("cliente"), cliente);
    }

    public static Specification<Servico> hasTecnico(Tecnico tecnico) {
        return (root, query, cb) -> cb.equal(root.get("tecnico"), tecnico);
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
