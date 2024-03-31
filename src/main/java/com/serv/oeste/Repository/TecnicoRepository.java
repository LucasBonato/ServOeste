package com.serv.oeste.Repository;

import com.serv.oeste.Models.Situacao;
import com.serv.oeste.Models.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    List<Tecnico> findAllBySituacao(Situacao situacao);
}
