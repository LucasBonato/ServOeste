package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.entities.technician.TechnicianAvailability;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.TechnicianFilter;

import java.util.List;
import java.util.Optional;

public interface ITechnicianRepository {
    Technician save(Technician technician);
    Optional<Technician> findById(Integer id);
    List<Technician> saveAll(List<Technician> technicians);
    List<TechnicianAvailability> getTechnicianAvailabilityBySpecialty(Integer days, Integer specialtyId);
    PageResponse<Technician> filter(TechnicianFilter filter, PageFilter pageFilter);
}
