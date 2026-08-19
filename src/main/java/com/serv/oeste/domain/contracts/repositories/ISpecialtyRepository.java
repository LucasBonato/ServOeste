package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.valueObjects.Specialty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ISpecialtyRepository {
    Optional<Specialty> findById(Integer id);
    Set<Specialty> findAllById(List<Integer> specialtyIds);
    Optional<Specialty> findByName(String conhecimento);
    Set<Specialty> findAll(boolean onlyActive);
    Specialty save(Specialty specialty);
    long countTechniciansBySpecialtyId(Integer specialtyId);
}