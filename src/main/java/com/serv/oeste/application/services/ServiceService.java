package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.requests.ServicoRequest;
import com.serv.oeste.application.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.application.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.exceptions.service.ServiceNotFoundException;
import com.serv.oeste.domain.valueObjects.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {
    private final ClientService clientService;
    private final TechnicianService technicianService;
    private final IServiceRepository serviceRepository;
    private final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    public ServicoResponse fetchOneById(Integer id) {
        logger.debug("DEBUG - Fetching service by ID: {}", id);
        Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("ERROR - Service with id={} not found", id);
                    return new ServiceNotFoundException();
                });
        logger.info("INFO - Service found: id={}", id);

        return new ServicoResponse(service);
    }
    
    @Cacheable("allServicos")
    public PageResponse<ServicoResponse> fetchListByFilter(
            ServicoRequestFilter servicoRequestFilter,
            PageFilterRequest pageFilter
    ) {
        logger.debug("DEBUG - Fetching services with filter: {}", servicoRequestFilter);
        PageResponse<ServicoResponse> servicos = serviceRepository
                .filter(servicoRequestFilter.toServiceFilter(), pageFilter.toPageFilter())
                .map(ServicoResponse::new);
        logger.info("INFO - Found {} services with filter: {}", servicos.getPage().getTotalPages(), servicoRequestFilter);
        return servicos;
    }

    public ServicoResponse create(ServicoRequest servicoRequest, Integer clienteId) {
        logger.info("INFO - Creating service for client id {}", clienteId);
        Client cliente = clientService.getClienteById(clienteId);
        Technician tecnico = (servicoRequest.idTecnico() != null)
                ? technicianService.getTecnicoById(servicoRequest.idTecnico())
                : null;

        Service novoServico = serviceRepository.save(servicoRequest.toDomain(cliente, tecnico));

        return new ServicoResponse(novoServico);
    }
    
    public ServicoResponse update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        logger.info("INFO - Updating service with Id: {}", id);
        Service servico = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("ERROR - Service with Id {} not found", id);
                    return new ServiceNotFoundException();
                });

        logger.debug("DEBUG - Fetching related client and technician...");
        Client cliente = clientService.getClienteById(servicoUpdateRequest.idCliente());
        Technician tecnico = technicianService.getTecnicoById(servicoUpdateRequest.idTecnico());

        servico.update(
                servicoUpdateRequest.equipamento(),
                servicoUpdateRequest.marca(),
                servicoUpdateRequest.filial(),
                servicoUpdateRequest.descricao(),
                servicoUpdateRequest.situacao(),
                servicoUpdateRequest.horarioPrevisto(),
                servicoUpdateRequest.valor(),
                servicoUpdateRequest.formaPagamento(),
                servicoUpdateRequest.valorPecas(),
                servicoUpdateRequest.valorComissao(),
                servicoUpdateRequest.dataPagamentoComissao(),
                servicoUpdateRequest.dataFechamento(),
                servicoUpdateRequest.dataInicioGarantia(),
                servicoUpdateRequest.dataFimGarantia(),
                servicoUpdateRequest.dataAtendimentoPrevisto(),
                servicoUpdateRequest.dataAtendimentoEfetiva(),
                cliente,
                tecnico
        );
        
        Service servicoUpdated = serviceRepository.save(servico);

        logger.info("INFO - Service with Id {} updated successfully", id);
        return new ServicoResponse(servicoUpdated);
    }
    
    public void deleteListByIds(List<Integer> ids) {
        logger.info("INFO - Deleting services with Ids: {}", ids);
        serviceRepository.deleteAllById(ids);
    }
}