package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.valueObjects.ClientFilter;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IClientJpaRepository;
import com.serv.oeste.infrastructure.specifications.ClientSpecifications;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientRepository implements IClientRepository {
    private final IClientJpaRepository clientJpaRepository;

    @Override
    public PageResponse<Client> filter(ClientFilter filter, PageFilter pageFilter) {
        Specification<ClientEntity> specification = new SpecificationBuilder<ClientEntity>()
                .addIf(StringUtils::isNotBlank, filter.nome(), ClientSpecifications::hasNome)
                .addIf(StringUtils::isNotBlank, filter.telefone(), ClientSpecifications::hasTelefone)
                .addIf(StringUtils::isNotBlank, filter.endereco(), ClientSpecifications::hasEndereco)
                .build();

        Pageable pageable = PageRequest.of(pageFilter.page(), pageFilter.size());

        Page<Client> clientsPaged = clientJpaRepository.findAll(specification, pageable)
                .map(ClientEntity::toClient);

        return new PageResponse<>(
                clientsPaged.getContent(),
                clientsPaged.getTotalPages(),
                clientsPaged.getNumber(),
                clientsPaged.getSize()
        );
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return clientJpaRepository.findById(id).map(ClientEntity::toClient);
    }

    @Override
    public List<Client> findAllByIds(List<Integer> ids) {
        return clientJpaRepository.findAllById(ids).stream()
                .map(ClientEntity::toClient)
                .toList();
    }

    @Override
    public Client save(Client client) {
        return clientJpaRepository.save(new ClientEntity(client)).toClient();
    }

    @Override
    public void deleteAllByIds(List<Integer> ids) {
        clientJpaRepository.deleteAllByIdInBatch(ids);
    }
}
