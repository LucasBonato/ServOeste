package com.serv.oeste.service;

import com.serv.oeste.exception.cliente.ClienteNotFoundException;
import com.serv.oeste.exception.cliente.ClienteNotValidException;
import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.cliente.ClienteSpecifications;
import com.serv.oeste.models.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.dtos.reponses.ClienteResponse;
import com.serv.oeste.models.dtos.requests.ClienteRequest;
import com.serv.oeste.models.specifications.SpecificationBuilder;
import com.serv.oeste.repository.ClienteRepository;
import com.serv.oeste.repository.ServicoRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    public static Integer idUltimoCliente;

    @Cacheable("clienteCache")
    public ResponseEntity<ClienteResponse> getOne(Integer id) {
        return ResponseEntity.ok(getClienteResponse(clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new)));
    }

    @Cacheable("allClientes")
    public ResponseEntity<List<ClienteResponse>> getBy(ClienteRequestFilter filtroRequest) {
        Specification<Cliente> specification = new SpecificationBuilder<Cliente>()
                .addIf(StringUtils::isNotBlank, filtroRequest.nome(), ClienteSpecifications::hasNome)
                .addIf(StringUtils::isNotBlank, filtroRequest.telefone(), ClienteSpecifications::hasTelefone)
                .addIf(StringUtils::isNotBlank, filtroRequest.endereco(), ClienteSpecifications::hasEndereco)
                .build();

        List<ClienteResponse> response = clienteRepository.findAll(specification)
                .stream()
                .map(this::getClienteResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ClienteResponse> create(ClienteRequest clienteRequest) {
        verificarRegraDeNegocio(clienteRequest);
        Cliente newCliente = new Cliente(clienteRequest);

        Cliente cliente = clienteRepository.save(newCliente);

        idUltimoCliente = cliente.getId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(getClienteResponse(cliente));
    }

    public ResponseEntity<ClienteResponse> update(Integer id, ClienteRequest clienteRequest) {
        Cliente cliente = getClienteById(id);
        verificarRegraDeNegocio(clienteRequest);
        cliente.setAll(clienteRequest);
        return ResponseEntity.ok(getClienteResponse(clienteRepository.save(cliente)));
    }

    public ResponseEntity<Void> deleteAList(List<Integer> ids) {
        List<Integer> clientesNaoExcluidos = new ArrayList<>();

        ids.stream()
                .filter(id -> clienteRepository.findById(id).isPresent())
                .filter(id -> {
                    Boolean possuiServicos = servicoRepository.existsByClienteId(id);
                    if (possuiServicos) {
                        clientesNaoExcluidos.add(id);
                    }
                    return !possuiServicos;
                })
                .forEach(clienteRepository::deleteById);

        if (!clientesNaoExcluidos.isEmpty()) {
            throw new ClienteNotValidException("O(s) seguinte(s) cliente(s) não foram excluído(s) por possuirem serviços atrelados a si: " + clientesNaoExcluidos, Codigo.CLIENTE);
        }

        return ResponseEntity.ok().build();
    }

    public Cliente getClienteById(Integer id) {
        return clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new);
    }
    private ClienteResponse getClienteResponse(Cliente cliente) {
        return new ClienteResponse(
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefoneFixo(),
            cliente.getTelefoneCelular(),
            cliente.getEndereco(),
            cliente.getBairro(),
            cliente.getMunicipio()
        );
    }
    private boolean contemNumero(String endereco) {
        return endereco.chars().anyMatch(Character::isDigit);
    }
    private void verificarRegraDeNegocio(ClienteRequest clienteRequest) {
        if(StringUtils.isBlank(clienteRequest.nome())) {
            throw new ClienteNotValidException("O Nome do cliente não pode ser vazio!", Codigo.NOMESOBRENOME);
        }
        if(clienteRequest.nome().length() < 2){
            throw new ClienteNotValidException(String.format("O Nome do cliente precisa ter no mínimo %d caracteres!", 2), Codigo.NOMESOBRENOME);
        }
        if(StringUtils.isBlank(clienteRequest.sobrenome())) {
            throw new ClienteNotValidException("Digite Nome e Sobrenome!", Codigo.NOMESOBRENOME);
        }
        if(clienteRequest.sobrenome().length() < 2){
            throw new ClienteNotValidException(String.format("O Sobrenome do cliente precisa ter no mínimo %d caracteres!", 2), Codigo.NOMESOBRENOME);
        }
        if(clienteRequest.telefoneCelular().isBlank() && clienteRequest.telefoneFixo().isBlank()) {
            throw new ClienteNotValidException("O cliente precisa ter no mínimo um telefone cadastrado!", Codigo.TELEFONES);
        }
        if(clienteRequest.telefoneCelular().length() < 11 && !clienteRequest.telefoneCelular().isEmpty()){
            throw new ClienteNotValidException("Telefone celular inválido!", Codigo.TELEFONECELULAR);
        }
        if(clienteRequest.telefoneFixo().length() < 10 && !clienteRequest.telefoneFixo().isEmpty()) {
            throw new ClienteNotValidException("Telefone Fixo Inválido!", Codigo.TELEFONEFIXO);
        }
        if(StringUtils.isBlank(clienteRequest.endereco())){
            throw new ClienteNotValidException("O Endereço é obrigatório", Codigo.ENDERECO);
        }
        if(!contemNumero(clienteRequest.endereco())) {
            throw new ClienteNotValidException("É necessário possuir número no Endereço", Codigo.ENDERECO);
        }
        if(StringUtils.isBlank(clienteRequest.municipio())) {
            throw new ClienteNotValidException("O Município é obrigatório", Codigo.MUNICIPIO);
        }
        if(StringUtils.isBlank(clienteRequest.bairro())) {
            throw new ClienteNotValidException("O Bairro é obrigatório", Codigo.BAIRRO);
        }
    }
}
