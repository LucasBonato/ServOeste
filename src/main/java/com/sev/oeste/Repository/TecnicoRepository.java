package com.sev.oeste.Repository;

import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    List<Tecnico> findAllBySituacao(Situacao situacao);
}
