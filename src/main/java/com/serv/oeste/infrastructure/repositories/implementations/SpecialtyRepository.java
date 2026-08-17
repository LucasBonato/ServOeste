package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.infrastructure.entities.technician.SpecialtyEntity;
import com.serv.oeste.infrastructure.repositories.jpa.ISpecialtyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpecialtyRepository implements ISpecialtyRepository {
    private final ISpecialtyJpaRepository specialtyJpaRepository;

    @Override
    public Optional<Specialty> findById(Integer id) {
        return specialtyJpaRepository.findById(id).map(SpecialtyEntity::toDomain);
    }

    @Override
    public List<Specialty> findAllById(List<Integer> specialtyIds) {
        return specialtyJpaRepository.findAllById(specialtyIds).stream()
                .map(SpecialtyEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Specialty> findByName(String conhecimento) {
        return specialtyJpaRepository.findByConhecimentoIgnoreCase(conhecimento)
                .map(SpecialtyEntity::toDomain);
    }

    @Override
    public List<Specialty> findAll(boolean onlyActive) {
        if (onlyActive) {
            return specialtyJpaRepository.findAllByAtivoTrueOrderByIdAsc().stream()
                    .map(SpecialtyEntity::toDomain)
                    .toList();
        }
        return specialtyJpaRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(SpecialtyEntity::toDomain)
                .toList();
    }

    @Override
    public Specialty save(Specialty specialty) {
        return specialtyJpaRepository.save(new SpecialtyEntity(specialty)).toDomain();
    }

    @Override
    public long countTechniciansBySpecialtyId(Integer specialtyId) {
        return specialtyJpaRepository.countTechniciansBySpecialtyId(specialtyId);
    }
}