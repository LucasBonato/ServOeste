package com.serv.oeste.repository;

import com.serv.oeste.models.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer>, JpaSpecificationExecutor<Servico> {
    Boolean existsByClienteId(Integer clienteId);
}
