package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.valueObjects.ServiceFilter;

import java.util.List;
import java.util.Optional;

public interface IServiceRepository {
    boolean existsByClienteId(Integer clienteId);
    List<Service> filter(ServiceFilter filter);
    Optional<Service> findById(Integer id);
    Service save(Service service);
    void deleteById(Integer id);
}
