package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.exceptions.client.ClientNotFoundException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;
import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import com.serv.oeste.infrastructure.entities.service.ServiceEntity;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IClientJpaRepository;
import com.serv.oeste.infrastructure.repositories.jpa.IServiceJpaRepository;
import com.serv.oeste.infrastructure.repositories.jpa.ITechnicianJpaRepository;
import com.serv.oeste.infrastructure.specifications.ServiceSpecifications;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServiceRepository implements IServiceRepository {
    private final IServiceJpaRepository serviceJpaRepository;
    private final IClientJpaRepository clientJpaRepository;
    private final ITechnicianJpaRepository technicianJpaRepository;

    @Override
    public PageResponse<Service> filter(ServiceFilter filter, PageFilter pageFilter) {
        Specification<ServiceEntity> specification = new SpecificationBuilder<ServiceEntity>()
                .addIfNotNull(filter.servicoId(), ServiceSpecifications::hasServicoId)
                .addIfNotNull(filter.clienteId(), id -> ServiceSpecifications.hasCliente(getClientById(id)))
                .addIfNotNull(filter.tecnicoId(), id -> ServiceSpecifications.hasTecnico(getTechnicianById(id)))
                .addIfNotNull(filter.situacao(), ServiceSpecifications::hasSituacao)
                .addIfNotNull(filter.garantia(), ServiceSpecifications::hasGarantia)
                .addIf(StringUtils::isNotBlank, filter.clienteNome(), ServiceSpecifications::hasNomeCliente)
                .addIf(StringUtils::isNotBlank, filter.tecnicoNome(), ServiceSpecifications::hasNomeTecnico)
                .addIf(StringUtils::isNotBlank, filter.equipamento(), ServiceSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, filter.filial(), ServiceSpecifications::hasFilial)
                .addIf(StringUtils::isNotBlank, filter.periodo(), ServiceSpecifications::hasHorarioPrevisto)
                .addDateRange(
                        filter.dataAtendimentoPrevistoAntes(),
                        filter.dataAtendimentoPrevistoDepois(),
                        ServiceSpecifications::isDataAtendimentoPrevistoBetween,
                        ServiceSpecifications::hasDataAtendimentoPrevisto
                )
                .addDateRange(
                        filter.dataAtendimentoEfetivoAntes(),
                        filter.dataAtendimentoEfetivoDepois(),
                        ServiceSpecifications::isDataAtendimentoEfetivoBetween,
                        ServiceSpecifications::hasDataAtendimentoEfetivo
                )
                .addDateRange(
                        filter.dataAberturaAntes(),
                        filter.dataAberturaDepois(),
                        ServiceSpecifications::isDataAberturaBetween,
                        ServiceSpecifications::hasDataAbertura
                )
                .build();

        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        Pageable pageable = PageRequest.of(pageFilter.page(), pageFilter.size(), sort);

        Page<Service> servicesPaged = serviceJpaRepository.findAll(specification, pageable)
                .map(ServiceEntity::toService);

        return new PageResponse<>(
                servicesPaged.getContent(),
                servicesPaged.getTotalPages(),
                servicesPaged.getNumber(),
                servicesPaged.getSize()
        );
    }

    @Override
    public Optional<Service> findById(Integer id) {
        return serviceJpaRepository.findById(id).map(ServiceEntity::toService);
    }

    @Override
    public Service save(Service service) {
        return serviceJpaRepository.save(new ServiceEntity(service)).toService();
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        serviceJpaRepository.deleteAllById(ids);
    }

    @Override
    public Set<Integer> findAllClientIdsWithServices(List<Integer> clientIds) {
        return serviceJpaRepository.findDistinctClienteIdsWithServices(clientIds);
    }

    private Client getClientById(Integer id) {
        return clientJpaRepository.findById(id).map(ClientEntity::toClient).orElseThrow(ClientNotFoundException::new);
    }

    private Technician getTechnicianById(Integer id) {
        return technicianJpaRepository.findById(id).map(TechnicianEntity::toTechnician).orElseThrow(TechnicianNotFoundException::new);
    }
}