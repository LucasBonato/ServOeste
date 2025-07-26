package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;

import java.util.Optional;

public interface IServiceRepository {
    boolean existsByClienteId(Integer clienteId);
    PageResponse<Service> filter(ServiceFilter filter, PageFilter pageFilter);
    Optional<Service> findById(Integer id);
    Service save(Service service);
    void deleteById(Integer id);
}
