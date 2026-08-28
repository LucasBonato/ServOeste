package com.serv.oeste.infrastructure.repositories.jpa;

import com.serv.oeste.infrastructure.entities.technician.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, Integer> {
    Optional<SpecialtyEntity> findByConhecimentoIgnoreCase(String conhecimento);
    List<SpecialtyEntity> findAllByAtivoTrueOrderByIdAsc();

    @Query("SELECT COUNT(t) FROM TechnicianEntity t JOIN t.especialidades e WHERE e.id = :id")
    long countTechniciansBySpecialtyId(@Param("id") Integer id);
}