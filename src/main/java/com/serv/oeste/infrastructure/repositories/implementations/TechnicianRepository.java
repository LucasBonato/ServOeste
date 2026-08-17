package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.TechnicianAvailabilityProjection;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.utils.StringUtils;
import com.serv.oeste.domain.valueObjects.TechnicianAvailability;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.TechnicianFilter;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import com.serv.oeste.infrastructure.repositories.jpa.ITechnicianJpaRepository;
import com.serv.oeste.infrastructure.repositories.projections.TechnicianListProjection;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import com.serv.oeste.infrastructure.specifications.TechnicianSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnicianRepository implements ITechnicianRepository {
    private final ITechnicianJpaRepository technicianJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Optional<Technician> findById(Integer id) {
        return technicianJpaRepository.findById(id).map(TechnicianEntity::toDomainSlim);
    }

    @Override
    public Optional<Technician> findByIdWithEspecialidades(Integer id) {
        return technicianJpaRepository.findWithEspecialidadesById(id).map(TechnicianEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technician> findAllById(List<Integer> ids) {
        return technicianJpaRepository.findAllById(ids).stream()
                .map(TechnicianEntity::toDomain)
                .toList();
    }

    @Override
    public Technician save(Technician technician) {
        return technicianJpaRepository.save(new TechnicianEntity(technician)).toDomain();
    }

    @Override
    public void saveAll(List<Technician> technicians) {
        technicianJpaRepository.saveAll(technicians.stream().map(TechnicianEntity::new).toList());
    }

    @Override
    public List<TechnicianAvailability> getTechnicianAvailabilityBySpecialty(Integer days, Integer specialtyId) {
        return technicianJpaRepository.getTechnicianAvailabilityBySpecialty(days, specialtyId).stream()
                .map(TechnicianRepository::toTechnicianAvailability)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Technician> filter(TechnicianFilter filter, PageFilter pageFilter) {
        Specification<TechnicianEntity> specification = new SpecificationBuilder<TechnicianEntity>()
                .addIfNotNull(filter.id(), TechnicianSpecifications::hasId)
                .addIf(StringUtils::isNotBlank, filter.nome(), TechnicianSpecifications::hasNomeCompleto)
                .addIf(StringUtils::isNotBlank, filter.situacao(), TechnicianSpecifications::hasSituacao)
                .addIf(StringUtils::isNotBlank, filter.equipamento(), TechnicianSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, filter.telefone(), TechnicianSpecifications::hasTelefone)
                .build();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<TechnicianListProjection> dataQuery = cb.createQuery(TechnicianListProjection.class);
        Root<TechnicianEntity> root = dataQuery.from(TechnicianEntity.class);
        dataQuery.select(cb.construct(
                        TechnicianListProjection.class,
                        root.get("id"),
                        root.get("nome"),
                        root.get("sobrenome"),
                        root.get("telefoneFixo"),
                        root.get("telefoneCelular"),
                        root.get("situacao")))
                .where(specification.toPredicate(root, dataQuery, cb))
                .orderBy(cb.desc(root.get("id")));

        List<TechnicianListProjection> projections = entityManager
                .createQuery(dataQuery)
                .setFirstResult(pageFilter.page() * pageFilter.size())
                .setMaxResults(pageFilter.size())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TechnicianEntity> countRoot = countQuery.from(TechnicianEntity.class);
        countQuery.select(cb.count(countRoot))
                .where(specification.toPredicate(countRoot, countQuery, cb));

        long totalElements = entityManager.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalElements / pageFilter.size());

        List<Technician> content = projections.stream()
                .map(TechnicianListProjection::toDomain)
                .toList();

        return new PageResponse<>(content, totalPages, pageFilter.page(), pageFilter.size());
    }

    private static TechnicianAvailability toTechnicianAvailability(TechnicianAvailabilityProjection projection) {
        return new TechnicianAvailability(
                projection.getId(),
                projection.getNome(),
                projection.getData(),
                projection.getDia(),
                projection.getPeriodo(),
                projection.getQuantidade()
        );
    }
}
