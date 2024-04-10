package com.serv.oeste.Repository;

import com.serv.oeste.Models.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
    List<String> findAllConhecimentos();
}
