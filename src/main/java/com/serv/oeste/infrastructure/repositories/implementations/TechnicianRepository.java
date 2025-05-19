package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.TechnicianAvailabilityProjection;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.entities.technician.TechnicianAvailability;
import com.serv.oeste.domain.valueObjects.TechnicianFilter;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import com.serv.oeste.infrastructure.repositories.jpa.ITechnicianJpaRepository;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import com.serv.oeste.infrastructure.specifications.TechnicianSpecifications;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnicianRepository implements ITechnicianRepository {
    private final ITechnicianJpaRepository technicianJpaRepository;

    @Override
    public Optional<Technician> findById(Integer id) {
        return technicianJpaRepository.findById(id).map(TechnicianEntity::toTechnician);
    }

    @Override
    public Technician save(Technician technician) {
        return technicianJpaRepository.save(new TechnicianEntity(technician)).toTechnician();
    }

    @Override
    public List<Technician> saveAll(List<Technician> technicians) {
        return technicianJpaRepository.saveAll(technicians.stream().map(TechnicianEntity::new).toList()).stream()
                .map(TechnicianEntity::toTechnician)
                .toList();
    }

    @Override
    public List<TechnicianAvailability> getTechnicianAvailabilityBySpecialty(Integer days, Integer specialtyId) {
        return technicianJpaRepository.getTechnicianAvailabilityBySpecialty(days, specialtyId).stream()
                .map(TechnicianRepository::toTechnicianAvailability)
                .collect(Collectors.toList());
    }

    @Override
    public List<Technician> filter(TechnicianFilter filter) {
        Specification<TechnicianEntity> specification = new SpecificationBuilder<TechnicianEntity>()
                .addIfNotNull(filter.id(), TechnicianSpecifications::hasId)
                .addIf(StringUtils::isNotBlank, filter.nome(), TechnicianSpecifications::hasNomeCompleto)
                .addIf(StringUtils::isNotBlank, filter.situacao(), TechnicianSpecifications::hasSituacao)
                .addIf(StringUtils::isNotBlank, filter.equipamento(), TechnicianSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, filter.telefone(), TechnicianSpecifications::hasTelefone)
                .build();

        return technicianJpaRepository.findAll(specification).stream()
                .map(TechnicianEntity::toTechnician)
                .toList();
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
