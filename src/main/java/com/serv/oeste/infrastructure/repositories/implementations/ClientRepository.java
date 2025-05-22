package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.valueObjects.ClientFilter;
import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IClientJpaRepository;
import com.serv.oeste.infrastructure.specifications.ClientSpecifications;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientRepository implements IClientRepository {
    private final IClientJpaRepository clientJpaRepository;

    @Override
    public List<Client> filter(ClientFilter filter) {
        Specification<ClientEntity> specification = new SpecificationBuilder<ClientEntity>()
                .addIf(StringUtils::isNotBlank, filter.nome(), ClientSpecifications::hasNome)
                .addIf(StringUtils::isNotBlank, filter.telefone(), ClientSpecifications::hasTelefone)
                .addIf(StringUtils::isNotBlank, filter.endereco(), ClientSpecifications::hasEndereco)
                .build();

        return clientJpaRepository.findAll(specification).stream()
                .map(ClientEntity::toClient)
                .toList();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return clientJpaRepository.findById(id).map(ClientEntity::toClient);
    }

    @Override
    public Client save(Client client) {
        return clientJpaRepository.save(new ClientEntity(client)).toClient();
    }

    @Override
    public void deleteById(Integer id) {
        clientJpaRepository.deleteById(id);
    }
}
