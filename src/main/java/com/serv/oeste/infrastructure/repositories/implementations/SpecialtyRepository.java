package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.infrastructure.entities.technician.SpecialtyEntity;
import com.serv.oeste.infrastructure.repositories.jpa.ISpecialtyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpecialtyRepository implements ISpecialtyRepository {
    private final ISpecialtyJpaRepository specialtyJpaRepository;

    @Override
    public Optional<Specialty> findById(Integer id) {
        return specialtyJpaRepository.findById(id).map(SpecialtyEntity::toSpecialty);
    }
}
