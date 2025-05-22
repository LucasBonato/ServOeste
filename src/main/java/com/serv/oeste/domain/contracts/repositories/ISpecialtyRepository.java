package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.specialty.Specialty;

import java.util.Optional;

public interface ISpecialtyRepository {
    Optional<Specialty> findById(Integer id);
}
