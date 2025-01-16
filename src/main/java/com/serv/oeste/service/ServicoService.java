package com.serv.oeste.service;

import com.serv.oeste.exception.servico.ServicoNotValidException;
import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.ClienteRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.enums.SituacaoServico;
import com.serv.oeste.models.servico.Servico;
import com.serv.oeste.models.servico.ServicoSpecifications;
import com.serv.oeste.models.specifications.SpecificationBuilder;
import com.serv.oeste.models.tecnico.Tecnico;
import com.serv.oeste.repository.ServicoRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;
    private final ServicoRepository servicoRepository;

    @Cacheable("allServicos")
    public ResponseEntity<List<ServicoResponse>> getByFilter(ServicoRequestFilter servicoRequestFilter) {
        Specification<Servico> specification = new SpecificationBuilder<Servico>()
                .addIfNotNull(servicoRequestFilter.servicoId(), ServicoSpecifications::hasServicoId)
                .addIfNotNull(servicoRequestFilter.clienteId(), id -> {
                    Cliente cliente = clienteService.getClienteById(id);
                    return ServicoSpecifications.hasCliente(cliente);
                })
                .addIfNotNull(servicoRequestFilter.tecnicoId(), id -> {
                    Tecnico tecnico = tecnicoService.getTecnicoById(id);
                    return ServicoSpecifications.hasTecnico(tecnico);
                })
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.clienteNome(), ServicoSpecifications::hasNomeCliente)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.tecnicoNome(), ServicoSpecifications::hasNomeTecnico)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.equipamento(), ServicoSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.filial(), ServicoSpecifications::hasFilial)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.periodo(), periodo -> {
                    String periodoFormatado = periodo.toLowerCase().replace("ã", "a");
                    return ServicoSpecifications.hasHorarioPrevisto(periodoFormatado);
                })
                .addDateRange(
                        servicoRequestFilter.dataAtendimentoPrevistoAntes(),
                        servicoRequestFilter.dataAtendimentoPrevistoDepois(),
                        ServicoSpecifications::isDataAtendimentoPrevistoBetween,
                        ServicoSpecifications::hasDataAtendimentoPrevisto
                )
                .addDateRange(
                        servicoRequestFilter.dataAtendimentoEfetivoAntes(),
                        servicoRequestFilter.dataAtendimentoEfetivoDepois(),
                        ServicoSpecifications::isDataAtendimentoEfetivoBetween,
                        ServicoSpecifications::hasDataAtendimentoEfetivo
                )
                .addDateRange(
                        servicoRequestFilter.dataAberturaAntes(),
                        servicoRequestFilter.dataAberturaDepois(),
                        ServicoSpecifications::isDataAberturaBetween,
                        ServicoSpecifications::hasDataAbertura
                )
                .build();
/*
        if (servicoRequestFilter.servicoId() != null) {
            specification = specification.and(ServicoSpecifications.hasServicoId(servicoRequestFilter.servicoId()));
        }
        if (servicoRequestFilter.clienteId() != null) {
            Cliente cliente = clienteService.getClienteById(servicoRequestFilter.clienteId());
            specification = specification.and(ServicoSpecifications.hasCliente(cliente));
        }
        if (servicoRequestFilter.tecnicoId() != null) {
            Tecnico tecnico = tecnicoService.getTecnicoById(servicoRequestFilter.tecnicoId());
            specification = specification.and(ServicoSpecifications.hasTecnico(tecnico));
        }
        if (StringUtils.isNotBlank(servicoRequestFilter.clienteNome())) {
            specification = specification.and(ServicoSpecifications.hasNomeCliente(servicoRequestFilter.clienteNome()));
        }
        if (StringUtils.isNotBlank(servicoRequestFilter.tecnicoNome())) {
            specification = specification.and(ServicoSpecifications.hasNomeTecnico(servicoRequestFilter.tecnicoNome()));
        }
        if (StringUtils.isNotBlank(servicoRequestFilter.equipamento())) {
            specification = specification.and(ServicoSpecifications.hasEquipamento(servicoRequestFilter.equipamento()));
        }
        if (StringUtils.isNotBlank(servicoRequestFilter.filial())) {
            specification = specification.and(ServicoSpecifications.hasFilial(servicoRequestFilter.filial()));
        }
        if (StringUtils.isNotBlank(servicoRequestFilter.periodo())) {
            String periodo = servicoRequestFilter.periodo().toLowerCase().replace("ã", "a");
            specification = specification.and(ServicoSpecifications.hasHorarioPrevisto(periodo));
        }
        if (servicoRequestFilter.dataAtendimentoPrevistoAntes() != null && servicoRequestFilter.dataAtendimentoPrevistoDepois() != null) {
            specification = specification.and(ServicoSpecifications.isDataAtendimentoPrevistoBetween(servicoRequestFilter.dataAtendimentoPrevistoAntes(), servicoRequestFilter.dataAtendimentoPrevistoDepois()));
        }
        else if (servicoRequestFilter.dataAtendimentoPrevistoAntes() != null) {
            specification = specification.and(ServicoSpecifications.hasDataAtendimentoPrevisto(servicoRequestFilter.dataAtendimentoPrevistoAntes()));
        }
        if (servicoRequestFilter.dataAtendimentoEfetivoAntes() != null && servicoRequestFilter.dataAtendimentoEfetivoDepois() != null) {
            specification = specification.and(ServicoSpecifications.isDataAtendimentoEfetivoBetween(servicoRequestFilter.dataAtendimentoEfetivoAntes(), servicoRequestFilter.dataAtendimentoEfetivoDepois()));
        }
        else if (servicoRequestFilter.dataAtendimentoEfetivoAntes() != null) {
            specification = specification.and(ServicoSpecifications.hasDataAtendimentoEfetivo(servicoRequestFilter.dataAtendimentoEfetivoAntes()));
        }
        if (servicoRequestFilter.dataAberturaAntes() != null && servicoRequestFilter.dataAberturaDepois() != null) {
            specification = specification.and(ServicoSpecifications.isDataAberturaBetween(servicoRequestFilter.dataAberturaAntes(), servicoRequestFilter.dataAberturaDepois()));
        }
        else if (servicoRequestFilter.dataAberturaAntes() != null) {
            specification = specification.and(ServicoSpecifications.hasDataAbertura(servicoRequestFilter.dataAberturaAntes()));
        }*/

        List<Servico> servicos = servicoRepository.findAll(specification);
        return ResponseEntity.ok(getServicosResponse(servicos));
    }

    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(ServicoRequest servicoRequest) {
        verificarSelecionamentoDasEntidades(servicoRequest);
        verificarCamposObrigatoriosServico(servicoRequest);
        ServicoResponse servicoResponse = cadastrarComVerificacoes(servicoRequest, servicoRequest.idCliente());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }

    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(ClienteRequest clienteRequest, ServicoRequest servicoRequest) {
        verificarCamposObrigatoriosServico(servicoRequest);
        clienteService.create(clienteRequest);
        verificarSelecionamentoDasEntidades(servicoRequest, ClienteService.idUltimoCliente);
        ServicoResponse servicoResponse = cadastrarComVerificacoes(servicoRequest, ClienteService.idUltimoCliente);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }

    public ResponseEntity<Void> deleteListOfServicesById(List<Integer> ids) {
        ids.stream()
                .filter(id -> servicoRepository.findById(id).isPresent())
                .forEach(servicoRepository::deleteById);

        return ResponseEntity.ok().build();
    }

    private List<ServicoResponse> getServicosResponse(List<Servico> servicos) {
        return servicos
                .stream()
                .map(this::getServicoResponse)
                .collect(Collectors.toList());
    }

    private ServicoResponse getServicoResponse(Servico servico) {
        return new ServicoResponse(
            servico.getId(),
            servico.getCliente().getId(),
            servico.getTecnico().getId(),
            servico.getCliente().getNome(),
            servico.getTecnico().getNome() + " " + servico.getTecnico().getSobrenome(),
            servico.getEquipamento(),
            servico.getFilial(),
            servico.getHorarioPrevisto(),
            servico.getMarca(),
            servico.getSituacao(),
            servico.getDataAtendimentoPrevisto()
        );
    }
    private static Date convertData(String data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dataFormatada;
        try{
            dataFormatada = formatter.parse(data);
        } catch (ParseException e){
            throw new ServicoNotValidException(Codigo.DATA, "Data em formato errado");
        }
        return dataFormatada;
    }

    private void verificarSelecionamentoDasEntidades(ServicoRequest servicoRequest) {
        if(servicoRequest.idCliente() == null) {
            throw new ServicoNotValidException(Codigo.CLIENTE, "Cliente não selecionado");
        }
        if(servicoRequest.idTecnico() == null) {
            throw new ServicoNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
    }
    private void verificarSelecionamentoDasEntidades(ServicoRequest servicoRequest, Integer idCliente) {
        if(idCliente == null) {
            throw new ServicoNotValidException(Codigo.CLIENTE, "Não foi possível encontrar o último cliente cadastrado");
        }
        if(servicoRequest.idTecnico() == null) {
            throw new ServicoNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
    }
    private void verificarCamposObrigatoriosServico(ServicoRequest servicoRequest) {
        if(StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServicoNotValidException(Codigo.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if(StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServicoNotValidException(Codigo.MARCA, "Marca é obrigatória");
        }
        if(StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição é obrigatória");
        }
        if(servicoRequest.descricao().length() < 10) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if(servicoRequest.descricao().split(" ").length < 2) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if(StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServicoNotValidException(Codigo.FILIAL, "A filial é obrigatória");
        }
    }
    private void verificarCamposNaoObrigatoriosServico(ServicoRequest servicoRequest) {
        if(StringUtils.isNotBlank(servicoRequest.dataAtendimento())) {
            convertData(servicoRequest.dataAtendimento());
        }
        if(StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServicoNotValidException(Codigo.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
    }
    private ServicoResponse cadastrarComVerificacoes(ServicoRequest servicoRequest, Integer idCliente){
        Cliente cliente = clienteService.getClienteById(idCliente);
        Tecnico tecnico = tecnicoService.getTecnicoById(servicoRequest.idTecnico());
        verificarCamposNaoObrigatoriosServico(servicoRequest);

        return cadastrarServico(servicoRequest, cliente, tecnico);
    }
    private ServicoResponse cadastrarServico(ServicoRequest servicoRequest, Cliente cliente, Tecnico tecnico) {
        SituacaoServico situacao = StringUtils.isBlank(servicoRequest.horarioPrevisto()) ? SituacaoServico.AGUARDANDO_AGENDAMENTO : SituacaoServico.AGUARDANDO_ATENDIMENTO;
        Servico novoServico = new Servico(
                servicoRequest.equipamento(),
                servicoRequest.marca(),
                servicoRequest.filial(),
                servicoRequest.descricao(),
                situacao,
                servicoRequest.horarioPrevisto(),
                StringUtils.isBlank(servicoRequest.dataAtendimento()) ? null : convertData(servicoRequest.dataAtendimento()),
                cliente,
                tecnico
        );
        return getServicoResponse(servicoRepository.save(novoServico));
    }
}