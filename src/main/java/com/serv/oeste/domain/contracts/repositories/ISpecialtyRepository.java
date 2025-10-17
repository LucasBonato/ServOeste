package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.valueObjects.Specialty;

import java.util.List;
import java.util.Optional;

public interface ISpecialtyRepository {
    Optional<Specialty> findById(Integer id);
    List<Specialty> findAllById(List<Integer> specialtyIds);
}
