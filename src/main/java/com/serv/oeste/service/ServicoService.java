package com.serv.oeste.service;

import com.serv.oeste.exception.servico.ServicoNotFoundException;
import com.serv.oeste.exception.servico.ServicoNotValidException;
import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.ClienteRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.models.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.enums.SituacaoServico;
import com.serv.oeste.models.servico.Servico;
import com.serv.oeste.models.servico.ServicoSpecifications;
import com.serv.oeste.models.specifications.SpecificationBuilder;
import com.serv.oeste.models.tecnico.Tecnico;
import com.serv.oeste.repository.ServicoRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;
    private final ServicoRepository servicoRepository;
    
    @Cacheable("allServicos")
    public ResponseEntity<List<ServicoResponse>> fetchListByFilter(ServicoRequestFilter servicoRequestFilter) {
        Specification<Servico> specification = new SpecificationBuilder<Servico>()
                .addIfNotNull(servicoRequestFilter.servicoId(), ServicoSpecifications::hasServicoId)
                .addIfNotNull(servicoRequestFilter.clienteId(), id -> ServicoSpecifications.hasCliente(clienteService.getClienteById(id)))
                .addIfNotNull(servicoRequestFilter.tecnicoId(), id -> ServicoSpecifications.hasTecnico(tecnicoService.getTecnicoById(id)))
                .addIfNotNull(servicoRequestFilter.situacao(), ServicoSpecifications::hasSituacao)
                .addIfNotNull(servicoRequestFilter.garantia(), ServicoSpecifications::hasGarantia)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.clienteNome(), ServicoSpecifications::hasNomeCliente)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.tecnicoNome(), ServicoSpecifications::hasNomeTecnico)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.equipamento(), ServicoSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.filial(), ServicoSpecifications::hasFilial)
                .addIf(StringUtils::isNotBlank, servicoRequestFilter.periodo(), ServicoSpecifications::hasHorarioPrevisto)
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
        
        List<Servico> servicos = servicoRepository.findAll(specification);
        return ResponseEntity.ok(getServicosResponse(servicos));
    }
    
    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(ServicoRequest servicoRequest) {
        verificarSelecionamentoDasEntidades(servicoRequest.idCliente());
        verificarCamposObrigatoriosServico(servicoRequest);
        ServicoResponse servicoResponse = cadastrarComVerificacoes(servicoRequest, servicoRequest.idCliente());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }
    
    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(ClienteRequest clienteRequest, ServicoRequest servicoRequest) {
        verificarCamposObrigatoriosServico(servicoRequest);
        clienteService.create(clienteRequest);
        verificarSelecionamentoDasEntidades(ClienteService.idUltimoCliente);
        ServicoResponse servicoResponse = cadastrarComVerificacoes(servicoRequest, ClienteService.idUltimoCliente);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }
    
    public ResponseEntity<ServicoResponse> update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        Servico servico = servicoRepository.findById(id).orElseThrow(ServicoNotFoundException::new);
        
        verificarSelecionamentoDasEntidades(servicoUpdateRequest.idCliente(), servicoUpdateRequest.idTecnico(), servicoUpdateRequest.situacao());
        verificarCamposUpdate(servicoUpdateRequest);
        Cliente cliente = clienteService.getClienteById(servicoUpdateRequest.idCliente());
        Tecnico tecnico = tecnicoService.getTecnicoById(servicoUpdateRequest.idTecnico());
        
        Servico servicoUpdated = servicoRepository.save(new Servico(
                id,
                servicoUpdateRequest,
                servico,
                cliente,
                tecnico,
                convertData(servicoUpdateRequest.dataFechamento()),
                convertData(servicoUpdateRequest.dataInicioGarantia()),
                convertData(servicoUpdateRequest.dataFimGarantia()),
                convertData(servicoUpdateRequest.dataAtendimentoPrevisto()),
                convertData(servicoUpdateRequest.dataAtendimentoEfetiva()),
                convertData(servicoUpdateRequest.dataPagamentoComissao())
        ));
        
        return ResponseEntity.ok(getServicoResponse(servicoUpdated));
    }
    
    public ResponseEntity<Void> deleteListByIds(List<Integer> ids) {
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
            throw new ServicoNotValidException(Codigo.DATA, "Data em formato errado, formato correto: dd/MM/YYYY");
        }
        return dataFormatada;
    }
    
    private void verificarSelecionamentoDasEntidades(Integer idCliente, Integer idTecnico, SituacaoServico situacao) {
        if (idCliente == null) {
            throw new ServicoNotValidException(Codigo.CLIENTE, "Cliente não selecionado");
        }
        if ((!situacao.equals(SituacaoServico.AGUARDANDO_AGENDAMENTO) && !situacao.equals(SituacaoServico.CANCELADO)) && idTecnico == null) {
            throw new ServicoNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
    }
    
    private void verificarSelecionamentoDasEntidades(Integer idCliente) {
        if (idCliente == null) {
            throw new ServicoNotValidException(Codigo.CLIENTE, "Não foi possível encontrar o último cliente cadastrado");
        }
    }
    
    private void verificarCamposObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServicoNotValidException(Codigo.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServicoNotValidException(Codigo.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 2) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServicoNotValidException(Codigo.FILIAL, "A filial é obrigatória");
        }
    }
    
    private void verificarCamposNaoObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isNotBlank(servicoRequest.dataAtendimento())) {
            convertData(servicoRequest.dataAtendimento());
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServicoNotValidException(Codigo.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
    }
    
    private void verificarCamposUpdate(ServicoUpdateRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServicoNotValidException(Codigo.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServicoNotValidException(Codigo.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 2) {
            throw new ServicoNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServicoNotValidException(Codigo.FILIAL, "A filial é obrigatória");
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServicoNotValidException(Codigo.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
        if (servicoRequest.valor() != null && servicoRequest.valor() < 0) {
            throw new ServicoNotValidException(Codigo.SERVICO, "O Valor não pode ser negativo.");
        }
        if (servicoRequest.valorComissao() != null && servicoRequest.valorComissao() < 0) {
            throw new ServicoNotValidException(Codigo.SERVICO, "O Valor da Comissão não pode ser negativo.");
        }
        if (servicoRequest.valorPecas() != null && servicoRequest.valorPecas() < 0) {
            throw new ServicoNotValidException(Codigo.SERVICO, "O Valor das Peças não pode ser negativo.");
        }
    }
    
    private ServicoResponse cadastrarComVerificacoes(ServicoRequest servicoRequest, Integer idCliente) {
        Cliente cliente = clienteService.getClienteById(idCliente);
        Tecnico tecnico = (servicoRequest.idTecnico() != null) ? tecnicoService.getTecnicoById(servicoRequest.idTecnico()) : null;
        verificarCamposNaoObrigatoriosServico(servicoRequest);
        
        return cadastrarServico(servicoRequest, cliente, tecnico);
    }
    
    private ServicoResponse cadastrarServico(ServicoRequest servicoRequest, Cliente cliente, Tecnico tecnico) {
        SituacaoServico situacao = (StringUtils.isBlank(servicoRequest.horarioPrevisto()) || (convertData(servicoRequest.dataAtendimento()) == null && tecnico == null))
                ? SituacaoServico.AGUARDANDO_AGENDAMENTO
                : SituacaoServico.AGUARDANDO_ATENDIMENTO;
        
        if (situacao.equals(SituacaoServico.AGUARDANDO_ATENDIMENTO) && tecnico == null) {
            throw new ServicoNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
        
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