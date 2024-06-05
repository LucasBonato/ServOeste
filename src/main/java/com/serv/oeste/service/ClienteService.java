package com.serv.oeste.service;

import com.serv.oeste.exception.cliente.ClienteNotFoundException;
import com.serv.oeste.exception.cliente.ClienteNotValidException;
import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.cliente.ClienteSpecifications;
import com.serv.oeste.models.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.dtos.reponses.ClienteResponse;
import com.serv.oeste.models.dtos.requests.ClienteRequest;
import com.serv.oeste.models.enums.Situacao;
import com.serv.oeste.models.tecnico.Tecnico;
import com.serv.oeste.repository.ClienteRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired private ClienteRepository clienteRepository;

    public ResponseEntity<ClienteResponse> getOne(Integer id) {
        return ResponseEntity.ok(getClienteResponse(clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new)));
    }

    public ResponseEntity<List<ClienteResponse>> getBy(ClienteRequestFilter filtroRequest) {
        Specification<Cliente> specification = Specification.where(null);

        if (StringUtils.isNotBlank(filtroRequest.nome()))
            specification = specification.and(ClienteSpecifications.hasNome(filtroRequest.nome()));
        if (StringUtils.isNotBlank(filtroRequest.telefoneCelular()))
            specification = specification.and(ClienteSpecifications.hasTelefoneCelular(filtroRequest.telefoneCelular()));
        if (StringUtils.isNotBlank(filtroRequest.telefoneFixo()))
            specification = specification.and(ClienteSpecifications.hasTelefoneFixo(filtroRequest.telefoneFixo()));
        if (StringUtils.isNotBlank(filtroRequest.endereco()))
            specification = specification.and(ClienteSpecifications.hasEndereco(filtroRequest.endereco()));

        List<ClienteResponse> response = new ArrayList<>();
        clienteRepository.findAll(specification).forEach(cliente -> response.add(getClienteResponse(cliente)));

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> create(ClienteRequest clienteRequest) {
        verificarRegraDeNegocio(clienteRequest);
        Cliente cliente = new Cliente(clienteRequest);
        clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Void> update(Integer id, ClienteRequest clienteRequest) {
        Cliente cliente = getClienteById(id);
        verificarRegraDeNegocio(clienteRequest);
        cliente.setAll(clienteRequest);
        clienteRepository.save(cliente);
        return ResponseEntity.ok().build();
    }

    private Cliente getClienteById(Integer id) {
        return clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new);
    }

    public ResponseEntity<Void> deletandoAList(List<Integer> ids) {
        ids
            .stream()
            .filter(id -> clienteRepository.findById(id).isPresent())
                //Adicionar outro filtro para verificar se o Cliente possui um serviço relacionado a entidade
            .forEach(id -> clienteRepository.deleteById(id));

        return ResponseEntity.ok().build();
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
        if(clienteRequest.telefoneFixo().length() < 11 && !clienteRequest.telefoneFixo().isEmpty()) {
            throw new ClienteNotValidException("Telefone Fixo Inválido!", Codigo.TELEFONEFIXO);
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
    private ClienteResponse getClienteResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefoneFixo(),
                cliente.getTelefoneCelular()
        );
    }
    private boolean contemNumero(String endereco) {
        char[] enderecoChars = endereco.toCharArray();
        for (char enderecoChar : enderecoChars) {
            if(Character.isDigit(enderecoChar)) return true;
        }
        return false;
    }
}
