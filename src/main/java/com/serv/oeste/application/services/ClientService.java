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
    private final IClientRepository clientRepository;
    private final IServiceRepository serviceRepository;
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Cacheable("clienteCache")
    public ClienteResponse fetchOneById(Integer id) {
        logger.debug("Fetching client by ID: {}", id);
        Client client = getClienteById(id);
        logger.info("Client found: id={}, nome={}", id, client.getNome());

        return getClienteResponse(client);
    }
    
    @Cacheable("allClientes")
    public PageResponse<ClienteResponse> fetchListByFilter(ClienteRequestFilter filtroRequest, PageFilterRequest pageFilterRequest) {
        logger.debug("Fetching clients with filter: {}", filtroRequest);
        PageResponse<ClienteResponse> clientsResponse = clientRepository.filter(
                    filtroRequest.toClientFilter(),
                    pageFilterRequest.toPageFilter()
                )
                .map(ClienteResponse::new);
        logger.info("Found {} clients with filter: {}", clientsResponse.getPage().totalPages(), filtroRequest);

        return clientsResponse;
    }
    
    public ClienteResponse create(ClienteRequest clienteRequest) {
        logger.info("Creating new client");

        Client cliente = clientRepository.save(clienteRequest.toClient());

        logger.info("Client Created successfully with id={}", cliente.getId());

        return getClienteResponse(cliente);
    }
    
    public ClienteResponse update(Integer id, ClienteRequest clienteRequest) {
        logger.info("Updating client id={}", id);

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
        logger.info("Client updated id={}", clientUpdated.getId());

        return getClienteResponse(cliente);
    }

    public void deleteListByIds(List<Integer> ids) {
        logger.info("Deleting clients by ids: {}", ids);
        if (ids == null || ids.isEmpty()) return;

        List<Client> clients = clientRepository.findAllByIds(ids);
        if (clients.isEmpty()) {
            logger.warn("No clients found for deletion");
            return;
        }

        Set<Integer> blockedIds = serviceRepository.findAllClientIdsWithServices(ids);

        if (!blockedIds.isEmpty()) {
            logger.warn("Clients with active services cannot be deleted: {}", blockedIds);
            throw new ClientNotValidException(
                    ErrorFields.CLIENTE,
                    "Os seguintes clientes não foram excluídos por possuírem serviços atrelados: " + blockedIds
            );
        }

        clientRepository.deleteAllByIds(ids);
        logger.info("Deleted {} clients successfully", ids.size());
    }

    public Client getClienteById(Integer id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("Client with id={} not found", id);
                    return new ClientNotFoundException();
                });
    }
    
    private ClienteResponse getClienteResponse(Client client) {
        return new ClienteResponse(client);
    }
}