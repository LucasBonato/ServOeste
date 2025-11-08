package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IServiceRepository {
    PageResponse<Service> filter(ServiceFilter filter, PageFilter pageFilter);
    Optional<Service> findById(Integer id);
    Service save(Service service);
    Set<Integer> findAllClientIdsWithServices(List<Integer> ids);
    void deleteAllById(List<Integer> ids);
}
