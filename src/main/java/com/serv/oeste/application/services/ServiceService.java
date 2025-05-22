package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ServicoRequest;
import com.serv.oeste.application.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.application.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.application.exceptions.service.ServiceNotFoundException;
import com.serv.oeste.application.exceptions.service.ServiceNotValidException;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Codigo;
import com.serv.oeste.domain.enums.SituacaoServico;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
public class ServiceService {
    private final ClientService clientService;
    private final TechnicianService technicianService;
    private final IServiceRepository serviceRepository;
    
    @Cacheable("allServicos")
    public ResponseEntity<List<ServicoResponse>> fetchListByFilter(ServicoRequestFilter servicoRequestFilter) {
        List<com.serv.oeste.domain.entities.service.Service> servicos = serviceRepository.filter(servicoRequestFilter.toServiceFilter());
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
        clientService.create(clienteRequest);
        verificarSelecionamentoDasEntidades(ClientService.idUltimoCliente);
        ServicoResponse servicoResponse = cadastrarComVerificacoes(servicoRequest, ClientService.idUltimoCliente);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoResponse);
    }
    
    public ResponseEntity<ServicoResponse> update(Integer id, ServicoUpdateRequest servicoUpdateRequest) {
        com.serv.oeste.domain.entities.service.Service servico = serviceRepository.findById(id).orElseThrow(ServiceNotFoundException::new);
        
        verificarSelecionamentoDasEntidades(servicoUpdateRequest.idCliente(), servicoUpdateRequest.idTecnico(), servicoUpdateRequest.situacao());
        verificarCamposUpdate(servicoUpdateRequest);
        Client cliente = clientService.getClienteById(servicoUpdateRequest.idCliente());
        Technician tecnico = technicianService.getTecnicoById(servicoUpdateRequest.idTecnico());
        
        com.serv.oeste.domain.entities.service.Service servicoUpdated = serviceRepository.save(new com.serv.oeste.domain.entities.service.Service(
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
        
        return ResponseEntity.ok(getServicoResponse(servicoUpdated));
    }
    
    public ResponseEntity<Void> deleteListByIds(List<Integer> ids) {
        ids.stream()
                .filter(id -> serviceRepository.findById(id).isPresent())
                .forEach(serviceRepository::deleteById);
        
        return ResponseEntity.ok().build();
    }
    
    private List<ServicoResponse> getServicosResponse(List<com.serv.oeste.domain.entities.service.Service> servicos) {
        return servicos
                .stream()
                .map(this::getServicoResponse)
                .collect(Collectors.toList());
    }
    
    private ServicoResponse getServicoResponse(com.serv.oeste.domain.entities.service.Service servico) {
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
            throw new ServiceNotValidException(Codigo.DATA, "Data em formato errado, formato correto: dd/MM/YYYY");
        }
        return dataFormatada;
    }
    
    private void verificarSelecionamentoDasEntidades(Integer idCliente, Integer idTecnico, SituacaoServico situacao) {
        if (idCliente == null) {
            throw new ServiceNotValidException(Codigo.CLIENTE, "Cliente não selecionado");
        }
        if ((!situacao.equals(SituacaoServico.AGUARDANDO_AGENDAMENTO) && !situacao.equals(SituacaoServico.CANCELADO)) && idTecnico == null) {
            throw new ServiceNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
    }
    
    private void verificarSelecionamentoDasEntidades(Integer idCliente) {
        if (idCliente == null) {
            throw new ServiceNotValidException(Codigo.CLIENTE, "Não foi possível encontrar o último cliente cadastrado");
        }
    }
    
    private void verificarCamposObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServiceNotValidException(Codigo.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServiceNotValidException(Codigo.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 2) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServiceNotValidException(Codigo.FILIAL, "A filial é obrigatória");
        }
    }
    
    private void verificarCamposNaoObrigatoriosServico(ServicoRequest servicoRequest) {
        if (StringUtils.isNotBlank(servicoRequest.dataAtendimento())) {
            convertData(servicoRequest.dataAtendimento());
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServiceNotValidException(Codigo.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
    }
    
    private void verificarCamposUpdate(ServicoUpdateRequest servicoRequest) {
        if (StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new ServiceNotValidException(Codigo.EQUIPAMENTO, "Equipamento é obrigatório");
        }
        if (StringUtils.isBlank(servicoRequest.marca())) {
            throw new ServiceNotValidException(Codigo.MARCA, "Marca é obrigatória");
        }
        if (StringUtils.isBlank(servicoRequest.descricao())) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição é obrigatória");
        }
        if (servicoRequest.descricao().length() < 10) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        }
        if (servicoRequest.descricao().split(" ").length < 2) {
            throw new ServiceNotValidException(Codigo.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        }
        if (StringUtils.isBlank(servicoRequest.filial())) {
            throw new ServiceNotValidException(Codigo.FILIAL, "A filial é obrigatória");
        }
        if (StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") && !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new ServiceNotValidException(Codigo.HORARIO, "Horário enviado de forma errada, manha ou tarde");
        }
        if (servicoRequest.valor() != null && servicoRequest.valor() < 0) {
            throw new ServiceNotValidException(Codigo.SERVICO, "O Valor não pode ser negativo.");
        }
        if (servicoRequest.valorComissao() != null && servicoRequest.valorComissao() < 0) {
            throw new ServiceNotValidException(Codigo.SERVICO, "O Valor da Comissão não pode ser negativo.");
        }
        if (servicoRequest.valorPecas() != null && servicoRequest.valorPecas() < 0) {
            throw new ServiceNotValidException(Codigo.SERVICO, "O Valor das Peças não pode ser negativo.");
        }
    }
    
    private ServicoResponse cadastrarComVerificacoes(ServicoRequest servicoRequest, Integer idCliente) {
        Client cliente = clientService.getClienteById(idCliente);
        Technician tecnico = (servicoRequest.idTecnico() != null) ? technicianService.getTecnicoById(servicoRequest.idTecnico()) : null;
        verificarCamposNaoObrigatoriosServico(servicoRequest);
        
        return cadastrarServico(servicoRequest, cliente, tecnico);
    }
    
    private ServicoResponse cadastrarServico(ServicoRequest servicoRequest, Client cliente, Technician tecnico) {
        SituacaoServico situacao = (StringUtils.isBlank(servicoRequest.horarioPrevisto()) || (convertData(servicoRequest.dataAtendimento()) == null && tecnico == null))
                ? SituacaoServico.AGUARDANDO_AGENDAMENTO
                : SituacaoServico.AGUARDANDO_ATENDIMENTO;
        
        if (situacao.equals(SituacaoServico.AGUARDANDO_ATENDIMENTO) && tecnico == null) {
            throw new ServiceNotValidException(Codigo.TECNICO, "Técnico não selecionado");
        }
        
        com.serv.oeste.domain.entities.service.Service novoServico = new com.serv.oeste.domain.entities.service.Service(
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
        return getServicoResponse(serviceRepository.save(novoServico));
    }
}