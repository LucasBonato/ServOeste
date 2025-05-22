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
    public static Integer idUltimoCliente;
    
    @Cacheable("clienteCache")
    public ResponseEntity<ClienteResponse> fetchOneById(Integer id) {
        return ResponseEntity.ok(getClienteResponse(getClienteById(id)));
    }
    
    @Cacheable("allClientes")
    public ResponseEntity<List<ClienteResponse>> fetchListByFilter(ClienteRequestFilter filtroRequest) {
        List<ClienteResponse> response = clientRepository.filter(filtroRequest.toClientFilter())
                .stream()
                .map(ClienteResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    public ResponseEntity<ClienteResponse> create(ClienteRequest clienteRequest) {
        verificarRegraDeNegocio(clienteRequest);
        
        Client cliente = clientRepository.save(clienteRequest.toClient());
        
        idUltimoCliente = cliente.getId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(getClienteResponse(cliente));
    }
    
    public ResponseEntity<ClienteResponse> update(Integer id, ClienteRequest clienteRequest) {
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
        return ResponseEntity.ok(getClienteResponse(clientRepository.save(cliente)));
    }
    
    public ResponseEntity<Void> deleteListByIds(List<Integer> ids) {
        List<Integer> clientesNaoExcluidos = new ArrayList<>();
        
        ids.stream()
                .filter(id -> clientRepository.findById(id).isPresent())
                .filter(id -> {
                    boolean possuiServicos = serviceRepository.existsByClienteId(id);
                    if (possuiServicos) {
                        clientesNaoExcluidos.add(id);
                    }
                    return !possuiServicos;
                })
                .forEach(clientRepository::deleteById);
        
        if (!clientesNaoExcluidos.isEmpty()) {
            throw new ClientNotValidException("O(s) seguinte(s) cliente(s) não foram excluído(s) por possuirem serviços atrelados a si: " + clientesNaoExcluidos, Codigo.CLIENTE);
        }
        
        return ResponseEntity.ok().build();
    }

    public Client getClienteById(Integer id) {
        return clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }
    
    private ClienteResponse getClienteResponse(Client client) {
        return new ClienteResponse(client);
    }
    
    private boolean contemNumero(String endereco) {
        return endereco.chars().anyMatch(Character::isDigit);
    }
    
    private void verificarRegraDeNegocio(ClienteRequest clienteRequest) {
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