package com.sev.oeste.Repository;

import com.sev.oeste.Tecnico.Models.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
    Especialidade findByConhecimento(String conhecimento);
}
