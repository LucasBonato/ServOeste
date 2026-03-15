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
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceService.class);

    private final ClientService clientService;
    private final TechnicianService technicianService;
    private final IServiceRepository serviceRepository;

    public ServicoResponse fetchOneById(Integer id) {
        LOGGER.debug("service.fetch-by-id.started id={}", id);
        Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("service.fetch-by-id.not-found id={}", id);
                    return new ServiceNotFoundException();
                });

        LOGGER.info("service.fetch-by-id.succeeded id={}", id);

        return new ServicoResponse(service);
    }
    
    @Cacheable("allServicos")
    public PageResponse<ServicoResponse> fetchListByFilter(
            ServicoRequestFilter servicoRequestFilter,
            PageFilterRequest pageFilter
    ) {
        LOGGER.debug("service.fetch-list.started filter={}", servicoRequestFilter);
        PageResponse<ServicoResponse> servicos = serviceRepository
                .filter(servicoRequestFilter.toServiceFilter(), pageFilter.toPageFilter())
                .map(ServicoResponse::new);

        LOGGER.debug(
                "service.fetch-list.completed totalElements={} totalPages={} filter={}",
                servicos.getPage().totalElements(),
                servicos.getPage().totalPages(),
                servicoRequestFilter
        );

        return servicos;
    }

    public ServicoResponse create(ServicoRequest servicoRequest, Integer clienteId) {
        LOGGER.info("service.create.started clientId={} technicianId={}",
                clienteId,
                servicoRequest.idTecnico()
        );

        Client cliente = clientService.getClienteById(clienteId);
        Technician tecnico = (servicoRequest.idTecnico() != null)
                ? technicianService.getTecnicoById(servicoRequest.idTecnico())
                : null;

        Service novoServico = serviceRepository.save(servicoRequest.toDomain(cliente, tecnico));

        LOGGER.info("service.create.completed id={} clientId={} technicianId={}",
                novoServico.getId(),
                clienteId,
                servicoRequest.idTecnico()
        );

        return new ServicoResponse(novoServico);
    }
    
    public ServicoResponse update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        LOGGER.info("service.update.started id={}", id);
        Service servico = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("service.update.not-found id={}", id);
                    return new ServiceNotFoundException();
                });

        LOGGER.debug("service.update.fetch-related-entities id={} clientId={} technicianId={}",
                id,
                servicoUpdateRequest.idCliente(),
                servicoUpdateRequest.idTecnico()
        );

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

        LOGGER.info("service.update.completed id={} clientId={} technicianId={}",
                id,
                servicoUpdateRequest.idCliente(),
                servicoUpdateRequest.idTecnico()
        );
        return new ServicoResponse(servicoUpdated);
    }

    public void deleteListByIds(List<Integer> ids) {
        LOGGER.info("service.delete-list.started ids={}", ids);
        serviceRepository.deleteAllById(ids);
        LOGGER.info("service.delete-list.completed count={}", ids != null ? ids.size() : 0);
    }
}