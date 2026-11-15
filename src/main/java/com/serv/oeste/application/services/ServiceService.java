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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {
    private final ClientService clientService;
    private final TechnicianService technicianService;
    private final IServiceRepository serviceRepository;

    public ServicoResponse fetchOneById(Integer id) {
        log.debug("service.fetch-by-id.started id={}", id);
        Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("service.fetch-by-id.not-found id={}", id);
                    return new ServiceNotFoundException();
                });

        log.info("service.fetch-by-id.succeeded id={}", id);

        return new ServicoResponse(service);
    }
    
    public PageResponse<ServicoResponse> fetchListByFilter(
            ServicoRequestFilter servicoRequestFilter,
            PageFilterRequest pageFilter
    ) {
        log.debug("service.fetch-list.started filter={}", servicoRequestFilter);
        PageResponse<ServicoResponse> servicos = serviceRepository
                .filter(servicoRequestFilter.toServiceFilter(), pageFilter.toPageFilter())
                .map(ServicoResponse::new);

        log.debug(
                "service.fetch-list.completed totalElements={} totalPages={} filter={}",
                servicos.getPage().totalElements(),
                servicos.getPage().totalPages(),
                servicoRequestFilter
        );

        return servicos;
    }

    public ServicoResponse create(ServicoRequest servicoRequest, Integer clienteId) {
        log.info("service.create.started clientId={} technicianId={}",
                clienteId,
                servicoRequest.idTecnico()
        );

        Client cliente = clientService.getClienteById(clienteId);
        Technician tecnico = (servicoRequest.idTecnico() != null)
                ? technicianService.getTecnicoById(servicoRequest.idTecnico())
                : null;

        Service novoServico = serviceRepository.save(servicoRequest.toDomain(cliente, tecnico));

        log.info("service.create.completed id={} clientId={} technicianId={}",
                novoServico.getId(),
                clienteId,
                servicoRequest.idTecnico()
        );

        return new ServicoResponse(novoServico);
    }

    public ServicoResponse update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        log.info("service.update.started id={}", id);
        Service servico = serviceRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("service.update.not-found id={}", id);
                    return new ServiceNotFoundException();
                });

        log.debug("service.update.fetch-related-entities id={} clientId={} technicianId={}",
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
                servicoUpdateRequest.periodo(),
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

        log.info("service.update.completed id={} clientId={} technicianId={}",
                id,
                servicoUpdateRequest.idCliente(),
                servicoUpdateRequest.idTecnico()
        );
        return new ServicoResponse(servicoUpdated);
    }

    public void deleteListByIds(List<Integer> ids) {
        log.info("service.delete-list.started ids={}", ids);
        serviceRepository.deleteAllById(ids);
        log.info("service.delete-list.completed count={}", ids != null ? ids.size() : 0);
    }
}