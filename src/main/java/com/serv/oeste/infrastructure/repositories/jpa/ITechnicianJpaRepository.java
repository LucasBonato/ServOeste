package com.serv.oeste.infrastructure.repositories.jpa;

import com.serv.oeste.domain.contracts.TechnicianAvailabilityProjection;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITechnicianJpaRepository extends JpaRepository<TechnicianEntity, Integer>, JpaSpecificationExecutor<TechnicianEntity> {
    @Query(value = "CALL GetTabelaDisponibilidade(:days, :specialtyId)", nativeQuery = true)
    List<TechnicianAvailabilityProjection> getTechnicianAvailabilityBySpecialty(@Param("days") Integer days, @Param("specialtyId") Integer specialtyId);
}
