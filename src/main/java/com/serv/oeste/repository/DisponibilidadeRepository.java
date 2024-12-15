package com.serv.oeste.repository;

import com.serv.oeste.models.tecnico.TecnicoDisponibilidadeRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<TecnicoDisponibilidadeRaw, Void> {
    @Query(value = "CALL GetTabelaDisponibilidade(:days, :especialidadeId)", nativeQuery = true)
    Optional<List<TecnicoDisponibilidadeRaw>> getDisponibilidadeTecnicosPeloConhecimento(@Param("days") Integer days, @Param("especialidadeId") Integer especialidadeId);
}
