package com.serv.oeste.repository;

import com.serv.oeste.models.tecnico.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
//    @Query("SELECT conhecimento FROM Especialidade")
//    List<String> findAllConhecimento();
}
