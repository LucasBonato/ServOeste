package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.domain.exceptions.service.ServiceNotFoundException;
import com.serv.oeste.domain.exceptions.service.ServiceNotValidException;
import com.serv.oeste.domain.valueObjects.PageResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ClientService clientService;
    private final TechnicianService technicianService;
    private final IServiceRepository serviceRepository;
    private final Logger logger = LoggerFactory.getLogger(ServiceService.class);
    
    @Cacheable("allServicos")
    public ResponseEntity<PageResponse<ServicoResponse>> fetchListByFilter(ServicoRequestFilter servicoRequestFilter, PageFilterRequest pageFilter) {
        logger.debug("DEBUG - Fetching services with filter: {}", servicoRequestFilter);
        PageResponse<ServicoResponse> servicos = serviceRepository.filter(servicoRequestFilter.toServiceFilter(), pageFilter.toPageFilter())
                .map(this::getServicoResponse);
        logger.info("INFO - Found {} services with filter: {}", servicos.getPage().getTotalPages(), servicoRequestFilter);
        return ResponseEntity.ok(servicos);
    }
    
    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(ServicoRequest servicoRequest) {
        logger.info("INFO - Creating new service");
        verificarSelecionamentoDasEntidades(servicoRequest.idCliente());
        verificarCamposObrigatoriosServico(servicoRequest);
        logger.info("DEBUG - Business validation passed for service");
        ServicoResponse servicoResponse = criarServico(servicoRequest, servicoRequest.idCliente());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }
    
    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(ClienteRequest clienteRequest, ServicoRequest servicoRequest) {
        logger.info("INFO - Creating new service and client");
        verificarCamposObrigatoriosServico(servicoRequest);
        logger.info("INFO - Creating new client for service");
        ClienteResponse cliente = clientService.create(clienteRequest);

        if (cliente == null) {
            logger.error("ERROR - Could not create client with: {}", clienteRequest);
            throw new ServiceNotValidException(ErrorFields.CLIENTE, "Não foi possível pegar o id do cliente!");
        }

        verificarSelecionamentoDasEntidades(cliente.id());
        logger.info("DEBUG - Business validation passed for service and client");

        ServicoResponse servicoResponse = criarServico(servicoRequest, cliente.id());
        logger.info("INFO - Service Created successfully with id={}, clientId={}, technicianId={}", servicoResponse.id(), servicoResponse.idCliente(), servicoResponse.idTecnico());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }
    
    public ResponseEntity<ServicoResponse> update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        logger.info("INFO - Updating service with Id: {}", id);
        Service servico = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("ERROR - Service with Id {} not found", id);
                    return new ServiceNotFoundException();
                });
        
        verificarSelecionamentoDasEntidades(servicoUpdateRequest.idCliente(), servicoUpdateRequest.idTecnico(), servicoUpdateRequest.situacao());
        verificarCamposUpdate(servicoUpdateRequest);

        logger.debug("DEBUG - Fetching related client and technician...");
        Client cliente = clientService.getClienteById(servicoUpdateRequest.idCliente());
        Technician tecnico = technicianService.getTecnicoById(servicoUpdateRequest.idTecnico());
        
        Service servicoUpdated = serviceRepository.save(new Service(
                id,
                servicoUpdateRequest.equipamento(),
                servicoUpdateRequest.marca(),
                servicoUpdateRequest.filial(),
                servicoUpdateRequest.descricao(),
                servico.getDescricao(),
                servicoUpdateRequest.situacao(),
                servicoUpdateRequest.horarioPrevisto(),
                servicoUpdateRequest.valor(),
                servicoUpdateRequest.formaPagamento(),
                servicoUpdateRequest.valorPecas(),
                servicoUpdateRequest.valorComissao(),
                convertData(servicoUpdateRequest.dataPagamentoComissao()),
                convertData(servicoUpdateRequest.dataFechamento()),
                convertData(servicoUpdateRequest.dataInicioGarantia()),
                convertData(servicoUpdateRequest.dataFimGarantia()),
                convertData(servicoUpdateRequest.dataAtendimentoPrevisto()),
                convertData(servicoUpdateRequest.dataAtendimentoEfetiva()),
                cliente,
                tecnico
        ));

        logger.info("INFO - Service with Id {} updated successfully", id);
        return ResponseEntity.ok(getServicoResponse(servicoUpdated));
    }
    
    public ResponseEntity<Void> deleteListByIds(List<Integer> ids) {
        logger.info("INFO - Deleting services with Ids: {}", ids);
        ids.stream()
                .filter(id -> serviceRepository.findById(id).isPresent())
                .forEach(serviceRepository::deleteById);
        
        return ResponseEntity.ok().build();
    }

    private ServicoResponse getServicoResponse(Service servico) {
        Boolean garatia = null;
        if (servico.getDataInicioGarantia() != null) {
            java.sql.Date dataHoje = java.sql.Date.valueOf(LocalDate.now());
            garatia = (servico.getDataInicioGarantia().before(dataHoje) && servico.getDataFimGarantia().after(dataHoje));
        }
        
        return new ServicoResponse(
                servico.getId(),
                servico.getCliente().getId(),
                (servico.getTecnico() != null) ? servico.getTecnico().getId() : null,
                servico.getCliente().getNome(),
                (servico.getTecnico() != null) ? servico.getTecnico().getNome() + " " + servico.getTecnico().getSobrenome() : null,
                servico.getEquipamento(),
                servico.getFilial(),
                servico.getHorarioPrevisto(),
                servico.getMarca(),
                servico.getDescricao(),
                (servico.getFormaPagamento() != null) ? servico.getFormaPagamento().getFormaPagamento() : null,
                servico.getSituacao(),
                garatia,
                servico.getValor(),
                servico.getValorComissao(),
                servico.getValorPecas(),
                servico.getDataAtendimentoPrevisto(),
                servico.getDataFechamento(),
                servico.getDataInicioGarantia(),
                servico.getDataFimGarantia(),
                servico.getDataAtendimentoEfetiva(),
                servico.getDataPagamentoComissao()
        );
    }
    
    private static Date convertData(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dataFormatada;
        try {
            dataFormatada = formatter.parse(data);
        }
        catch (ParseException e) {
            throw new ServiceNotValidException(ErrorFields.DATA, "Data em formato errado, formato correto: dd/MM/YYYY");
        }
        return dataFormatada;
    }
    
    private void verificarSelecionamentoDasEntidades(Integer idCliente, Integer idTecnico, SituacaoServico situacao) {
        if (idCliente == null) {
            throw new ServiceNotValidException(ErrorFields.CLIENTE, "Cliente não selecionado");
        }
        if ((!situacao.equals(SituacaoServico.AGUARDANDO_AGENDAMENTO) && !situacao.equals(SituacaoServico.CANCELADO)) && idTecnico == null) {
            throw new ServiceNotValidException(ErrorFields.TECNICO, "Técnico não selecionado");
        }
    }
    
    protected void verificarSelecionamentoDasEntidades(Integer idCliente) {
        if (idCliente == null) {
            throw new ServiceNotValidException(ErrorFields.CLIENTE, "Não foi possível encontrar o último cliente cadastrado!");
        }
    }
    
    private void verificarCamposObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServiceNotValidException(ErrorFields.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServiceNotValidException(ErrorFields.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 3) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServiceNotValidException(ErrorFields.FILIAL, "A filial é obrigatória");
        }
    }
    
    private void verificarCamposNaoObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isNotBlank(servicoRequest.dataAtendimento())) {
            convertData(servicoRequest.dataAtendimento());
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServiceNotValidException(ErrorFields.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
    }
    
    private void verificarCamposUpdate(ServicoUpdateRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServiceNotValidException(ErrorFields.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServiceNotValidException(ErrorFields.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 2) {
            throw new ServiceNotValidException(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServiceNotValidException(ErrorFields.FILIAL, "A filial é obrigatória");
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServiceNotValidException(ErrorFields.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
        if (servicoRequest.valor() != null && servicoRequest.valor() < 0) {
            throw new ServiceNotValidException(ErrorFields.SERVICO, "O Valor não pode ser negativo.");
        }
        if (servicoRequest.valorComissao() != null && servicoRequest.valorComissao() < 0) {
            throw new ServiceNotValidException(ErrorFields.SERVICO, "O Valor da Comissão não pode ser negativo.");
        }
        if (servicoRequest.valorPecas() != null && servicoRequest.valorPecas() < 0) {
            throw new ServiceNotValidException(ErrorFields.SERVICO, "O Valor das Peças não pode ser negativo.");
        }
    }
    
    protected ServicoResponse criarServico(ServicoRequest servicoRequest, Integer idCliente) {
        logger.info("INFO - Creating service for client id {}", idCliente);
        Client cliente = clientService.getClienteById(idCliente);
        Technician tecnico = (servicoRequest.idTecnico() != null) ? technicianService.getTecnicoById(servicoRequest.idTecnico()) : null;
        verificarCamposNaoObrigatoriosServico(servicoRequest);
        
        return cadastrarServico(servicoRequest, cliente, tecnico);
    }
    
    protected ServicoResponse cadastrarServico(ServicoRequest servicoRequest, Client cliente, Technician tecnico) {
        SituacaoServico situacao = (StringUtils.isBlank(servicoRequest.horarioPrevisto()) || (convertData(servicoRequest.dataAtendimento()) == null && tecnico == null))
                ? SituacaoServico.AGUARDANDO_AGENDAMENTO
                : SituacaoServico.AGUARDANDO_ATENDIMENTO;
        
        if (situacao.equals(SituacaoServico.AGUARDANDO_ATENDIMENTO) && tecnico == null) {
            logger.warn("WARN - Technician not selected but situation needs one");
            throw new ServiceNotValidException(ErrorFields.TECNICO, "Técnico não selecionado");
        }
        
        Service novoServico = serviceRepository.save(new Service(
                servicoRequest.equipamento(),
                servicoRequest.marca(),
                servicoRequest.filial(),
                servicoRequest.descricao(),
                situacao,
                servicoRequest.horarioPrevisto(),
                StringUtils.isBlank(servicoRequest.dataAtendimento()) ? null : convertData(servicoRequest.dataAtendimento()),
                cliente,
                tecnico
        ));

        logger.info("Service created successfully for client ID {}", cliente.getId());
        return getServicoResponse(novoServico);
    }
}