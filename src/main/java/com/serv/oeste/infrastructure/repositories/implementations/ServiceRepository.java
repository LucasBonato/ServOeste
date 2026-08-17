package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.utils.StringUtils;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;
import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import com.serv.oeste.infrastructure.entities.service.ServiceEntity;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IServiceJpaRepository;
import com.serv.oeste.infrastructure.repositories.projections.ServiceListProjection;
import com.serv.oeste.infrastructure.specifications.ServiceSpecifications;
import com.serv.oeste.infrastructure.specifications.SpecificationBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServiceRepository implements IServiceRepository {
    private final IServiceJpaRepository serviceJpaRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Service> filter(ServiceFilter filter, PageFilter pageFilter) {
        Specification<ServiceEntity> specification = new SpecificationBuilder<ServiceEntity>()
                .addIfNotNull(filter.servicoId(), ServiceSpecifications::hasServicoId)
                .addIfNotNull(filter.clienteId(), ServiceSpecifications::hasClienteId)
                .addIfNotNull(filter.tecnicoId(), ServiceSpecifications::hasTecnicoId)
                .addIfNotNull(filter.situacao(), ServiceSpecifications::hasSituacao)
                .addIfNotNull(filter.garantia(), ServiceSpecifications::hasGarantia)
                .addIf(StringUtils::isNotBlank, filter.clienteNome(), ServiceSpecifications::hasNomeCliente)
                .addIf(StringUtils::isNotBlank, filter.tecnicoNome(), ServiceSpecifications::hasNomeTecnico)
                .addIf(StringUtils::isNotBlank, filter.equipamento(), ServiceSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, filter.marca(), ServiceSpecifications::hasMarca)
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

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ServiceListProjection> dataQuery = cb.createQuery(ServiceListProjection.class);
        Root<ServiceEntity> root = dataQuery.from(ServiceEntity.class);
        Join<ServiceEntity, ClientEntity> clienteJoin = root.join("cliente", JoinType.LEFT);
        Join<ServiceEntity, TechnicianEntity> tecnicoJoin = root.join("tecnico", JoinType.LEFT);
        dataQuery.select(cb.construct(
                        ServiceListProjection.class,
                        root.get("id"),
                        root.get("equipamento"),
                        root.get("marca"),
                        root.get("filial"),
                        root.get("descricao"),
                        root.get("situacao"),
                        root.get("horarioPrevisto"),
                        root.get("valor"),
                        root.get("formaPagamento"),
                        root.get("valorPecas"),
                        root.get("valorComissao"),
                        root.get("dataPagamentoComissao"),
                        root.get("dataAbertura"),
                        root.get("dataFechamento"),
                        root.get("dataInicioGarantia"),
                        root.get("dataFimGarantia"),
                        root.get("dataAtendimentoPrevisto"),
                        root.get("dataAtendimentoEfetiva"),
                        clienteJoin.get("id"),
                        clienteJoin.get("nome"),
                        tecnicoJoin.get("id"),
                        tecnicoJoin.get("nome"),
                        tecnicoJoin.get("sobrenome")))
                .where(specification.toPredicate(root, dataQuery, cb))
                .orderBy(cb.desc(root.get("id")));

        List<ServiceListProjection> projections = entityManager
                .createQuery(dataQuery)
                .setFirstResult(pageFilter.page() * pageFilter.size())
                .setMaxResults(pageFilter.size())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ServiceEntity> countRoot = countQuery.from(ServiceEntity.class);
        countQuery.select(cb.count(countRoot))
                .where(specification.toPredicate(countRoot, countQuery, cb));

        long totalElements = entityManager.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalElements / pageFilter.size());

        List<Service> content = projections.stream()
                .map(ServiceListProjection::toDomain)
                .toList();

        return new PageResponse<>(content, totalPages, pageFilter.page(), pageFilter.size());
    }

    @Override
    public Optional<Service> findById(Integer id) {
        return serviceJpaRepository.findById(id).map(ServiceEntity::toDomain);
    }

    @Override
    public Service save(Service service) {
        return serviceJpaRepository.save(new ServiceEntity(service)).toDomain();
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        serviceJpaRepository.deleteAllById(ids);
    }

    @Override
    public Set<Integer> findAllClientIdsWithServices(List<Integer> clientIds) {
        return serviceJpaRepository.findDistinctClienteIdsWithServices(clientIds);
    }
}