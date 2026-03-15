package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.client.ClientNotFoundException;
import com.serv.oeste.domain.exceptions.client.ClientNotValidException;
import com.serv.oeste.domain.valueObjects.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    private final IClientRepository clientRepository;
    private final IServiceRepository serviceRepository;

    @Cacheable("clienteCache")
    public ClienteResponse fetchOneById(Integer id) {
        LOGGER.debug("client.fetch-by-id.started id={}", id);
        Client client = getClienteById(id);
        LOGGER.info("client.fetch-by-id.succeeded id={} nome={}", id, client.getNome());

        return getClienteResponse(client);
    }
    
    @Cacheable("allClientes")
    public PageResponse<ClienteResponse> fetchListByFilter(ClienteRequestFilter filtroRequest, PageFilterRequest pageFilterRequest) {
        LOGGER.debug("client.fetch-list.started filter={}", filtroRequest);
        PageResponse<ClienteResponse> clientsResponse = clientRepository.filter(
                        filtroRequest.toClientFilter(),
                        pageFilterRequest.toPageFilter()
                )
                .map(ClienteResponse::new);

        LOGGER.debug(
                "client.fetch-list.completed totalElements={} totalPages={} filter={}",
                clientsResponse.getPage().totalElements(),
                clientsResponse.getPage().totalPages(),
                filtroRequest
        );

        return clientsResponse;
    }
    
    public ClienteResponse create(ClienteRequest clienteRequest) {
        LOGGER.info("client.create.started nome={}", clienteRequest.nome());

        Client cliente = clientRepository.save(clienteRequest.toClient());

        LOGGER.info("client.create.completed id={} nome={}", cliente.getId(), cliente.getNome());

        return getClienteResponse(cliente);
    }
    
    public ClienteResponse update(Integer id, ClienteRequest clienteRequest) {
        LOGGER.info("client.update.started id={}", id);

        Client cliente = getClienteById(id);
        cliente.update(
                clienteRequest.nome(),
                clienteRequest.sobrenome(),
                clienteRequest.telefoneFixo(),
                clienteRequest.telefoneCelular(),
                clienteRequest.endereco(),
                clienteRequest.bairro(),
                clienteRequest.municipio()
        );

        Client clientUpdated = clientRepository.save(cliente);
        LOGGER.info("client.update.completed id={} nome={}", clientUpdated.getId(), clientUpdated.getNome());

        return getClienteResponse(cliente);
    }

    public void deleteListByIds(List<Integer> ids) {
        LOGGER.info("client.delete-list.started ids={}", ids);
        if (ids == null || ids.isEmpty()) return;

        List<Client> clients = clientRepository.findAllByIds(ids);
        if (clients.isEmpty()) {
            LOGGER.warn("client.delete-list.no-clients-found ids={}", ids);
            return;
        }

        Set<Integer> blockedIds = serviceRepository.findAllClientIdsWithServices(ids);

        if (!blockedIds.isEmpty()) {
            LOGGER.warn("client.delete-list.blocked-by-active-services blockedIds={}", blockedIds);
            throw new ClientNotValidException(
                    ErrorFields.CLIENTE,
                    "Os seguintes clientes não foram excluídos por possuírem serviços atrelados: " + blockedIds
            );
        }

        clientRepository.deleteAllByIds(ids);
        LOGGER.info("client.delete-list.completed count={}", ids.size());
    }

    public Client getClienteById(Integer id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("client.not-found id={}", id);
                    return new ClientNotFoundException();
                });
    }
    
    private ClienteResponse getClienteResponse(Client client) {
        return new ClienteResponse(client);
    }
}