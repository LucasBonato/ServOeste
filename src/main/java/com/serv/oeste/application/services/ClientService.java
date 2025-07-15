package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.exceptions.client.ClientNotFoundException;
import com.serv.oeste.application.exceptions.client.ClientNotValidException;
import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.enums.Codigo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final IClientRepository clientRepository;
    private final IServiceRepository serviceRepository;
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Cacheable("clienteCache")
    public ResponseEntity<ClienteResponse> fetchOneById(Integer id) {
        logger.debug("DEBUG - Fetching client by ID: {}", id);
        Client client = getClienteById(id);
        logger.info("INFO - Client found: id={}, nome={}", id, client.getNome());

        return ResponseEntity.ok(getClienteResponse(client));
    }
    
    @Cacheable("allClientes")
    public ResponseEntity<List<ClienteResponse>> fetchListByFilter(ClienteRequestFilter filtroRequest) {
        logger.debug("DEBUG - Fetching clients with filter: {}", filtroRequest);
        List<ClienteResponse> response = clientRepository.filter(filtroRequest.toClientFilter())
                .stream()
                .map(ClienteResponse::new)
                .collect(Collectors.toList());
        logger.info("INFO - Found {} clients with filter: {}", response.size(), filtroRequest);

        return ResponseEntity.ok(response);
    }
    
    public ResponseEntity<ClienteResponse> create(ClienteRequest clienteRequest) {
        logger.info("INFO - Creating new client");
        verificarRegraDeNegocio(clienteRequest);
        logger.debug("DEBUG - Business validation passed for client");
        
        Client cliente = clientRepository.save(clienteRequest.toClient());
        logger.info("INFO - Client Created successfully with id={}", cliente.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(getClienteResponse(cliente));
    }
    
    public ResponseEntity<ClienteResponse> update(Integer id, ClienteRequest clienteRequest) {
        logger.info("INFO - Updating client id={}", id);

        Client cliente = getClienteById(id);
        verificarRegraDeNegocio(clienteRequest);
        cliente.setAll(
                clienteRequest.nome(),
                clienteRequest.sobrenome(),
                clienteRequest.telefoneFixo(),
                clienteRequest.telefoneCelular(),
                clienteRequest.endereco(),
                clienteRequest.bairro(),
                clienteRequest.municipio()
        );

        Client clientUpdated = clientRepository.save(cliente);
        logger.info("INFO - Client updated id={}", clientUpdated.getId());

        return ResponseEntity.ok(getClienteResponse(cliente));
    }
    
    public ResponseEntity<Void> deleteListByIds(List<Integer> ids) {
        logger.info("INFO - Deleting clients by ids: {}", ids);
        List<Integer> clientesNaoExcluidos = new ArrayList<>();
        
        ids.stream()
                .filter(id -> clientRepository.findById(id).isPresent())
                .filter(id -> {
                    boolean possuiServicos = serviceRepository.existsByClienteId(id);
                    if (possuiServicos) {
                        logger.warn("WARN - Client id={} has associated services and cannot be deleted", id);
                        clientesNaoExcluidos.add(id);
                    }
                    return !possuiServicos;
                })
                .forEach(id -> {
                    clientRepository.deleteById(id);
                    logger.info("INFO - Client id={} deleted", id);
                });
        
        if (!clientesNaoExcluidos.isEmpty()) {
            logger.error("ERROR - Some clients could not be deleted due to service relations: {}", clientesNaoExcluidos);
            throw new ClientNotValidException("O(s) seguinte(s) cliente(s) não foram excluído(s) por possuirem serviços atrelados a si: " + clientesNaoExcluidos, Codigo.CLIENTE);
        }
        
        return ResponseEntity.ok().build();
    }

    public Client getClienteById(Integer id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("ERROR - Client with id={} not found", id);
                    return new ClientNotFoundException();
                });
    }
    
    private ClienteResponse getClienteResponse(Client client) {
        return new ClienteResponse(client);
    }
    
    private boolean contemNumero(String endereco) {
        return endereco.chars().anyMatch(Character::isDigit);
    }
    
    private void verificarRegraDeNegocio(ClienteRequest clienteRequest) {
        logger.debug("DEBUG - Validating business rules for client: nome={}, sobrenome={}", clienteRequest.nome(), clienteRequest.sobrenome());

        if (StringUtils.isBlank(clienteRequest.nome())) {
            throw new ClientNotValidException("O Nome do cliente não pode ser vazio!", Codigo.NOMESOBRENOME);
        }
        if (clienteRequest.nome().length() < 2) {
            throw new ClientNotValidException(String.format("O Nome do cliente precisa ter no mínimo %d caracteres!", 2), Codigo.NOMESOBRENOME);
        }
        if (StringUtils.isBlank(clienteRequest.sobrenome())) {
            throw new ClientNotValidException("Digite Nome e Sobrenome!", Codigo.NOMESOBRENOME);
        }
        if (clienteRequest.sobrenome().length() < 2) {
            throw new ClientNotValidException(String.format("O Sobrenome do cliente precisa ter no mínimo %d caracteres!", 2), Codigo.NOMESOBRENOME);
        }
        if (clienteRequest.telefoneCelular().isBlank() && clienteRequest.telefoneFixo().isBlank()) {
            throw new ClientNotValidException("O cliente precisa ter no mínimo um telefone cadastrado!", Codigo.TELEFONES);
        }
        if (clienteRequest.telefoneCelular().length() != 11 && !clienteRequest.telefoneCelular().isEmpty()) {
            throw new ClientNotValidException("Telefone celular inválido!", Codigo.TELEFONECELULAR);
        }
        if (clienteRequest.telefoneFixo().length() != 10 && !clienteRequest.telefoneFixo().isEmpty()) {
            throw new ClientNotValidException("Telefone fixo inválido!", Codigo.TELEFONEFIXO);
        }
        if (StringUtils.isBlank(clienteRequest.endereco())) {
            throw new ClientNotValidException("O Endereço é obrigatório!", Codigo.ENDERECO);
        }
        if (!contemNumero(clienteRequest.endereco())) {
            throw new ClientNotValidException("É necessário possuir número no Endereço!", Codigo.ENDERECO);
        }
        if (StringUtils.isBlank(clienteRequest.municipio())) {
            throw new ClientNotValidException("O Município é obrigatório!", Codigo.MUNICIPIO);
        }
        if (StringUtils.isBlank(clienteRequest.bairro())) {
            throw new ClientNotValidException("O Bairro é obrigatório!", Codigo.BAIRRO);
        }
    }
}