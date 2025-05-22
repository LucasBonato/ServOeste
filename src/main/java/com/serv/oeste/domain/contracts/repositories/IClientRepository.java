package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.valueObjects.ClientFilter;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {
    List<Client> filter(ClientFilter filter);
    Optional<Client> findById(Integer id);
    Client save(Client client);
    void deleteById(Integer id);
}