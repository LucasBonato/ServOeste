package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.valueObjects.ClientFilter;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;

import java.util.Optional;

public interface IClientRepository {
    PageResponse<Client> filter(ClientFilter filter, PageFilter pageFilter);
    Optional<Client> findById(Integer id);
    Client save(Client client);
    void deleteById(Integer id);
}