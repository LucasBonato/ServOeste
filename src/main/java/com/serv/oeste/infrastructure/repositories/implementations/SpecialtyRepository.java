package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.infrastructure.entities.technician.SpecialtyEntity;
import com.serv.oeste.infrastructure.repositories.jpa.ISpecialtyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpecialtyRepository implements ISpecialtyRepository {
    private final ISpecialtyJpaRepository specialtyJpaRepository;

    @Override
    public Optional<Specialty> findById(Integer id) {
        return specialtyJpaRepository.findById(id).map(SpecialtyEntity::toSpecialty);
    }

    @Override
    public List<Specialty> findAllById(List<Integer> specialtyIds) {
        return specialtyJpaRepository.findAllById(specialtyIds).stream()
                .map(SpecialtyEntity::toSpecialty)
                .toList();
    }
}
