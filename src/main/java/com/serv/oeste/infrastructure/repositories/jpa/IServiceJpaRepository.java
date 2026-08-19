package com.serv.oeste.infrastructure.repositories.jpa;

import com.serv.oeste.infrastructure.entities.service.ServiceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IServiceJpaRepository extends JpaRepository<ServiceEntity, Integer>, JpaSpecificationExecutor<ServiceEntity> {
    @NonNull
    @Override
    @EntityGraph(attributePaths = { "cliente", "tecnico" })
    Page<ServiceEntity> findAll(@NonNull Specification<ServiceEntity> spec, @NonNull Pageable pageable);

    @Query("SELECT DISTINCT s.cliente.id FROM ServiceEntity s WHERE s.cliente.id IN :ids")
    Set<Integer> findDistinctClienteIdsWithServices(@Param("ids") List<Integer> ids);
}
