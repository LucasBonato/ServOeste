package com.serv.oeste.service;

import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.enums.SituacaoServico;
import com.serv.oeste.models.servico.Servico;
import com.serv.oeste.models.tecnico.Tecnico;
import com.serv.oeste.repository.ClienteRepository;
import com.serv.oeste.repository.ServicoRepository;
import com.serv.oeste.repository.TecnicoRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class ServicoService {
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private ServicoRepository servicoRepository;

    public ResponseEntity<Void> cadastrarComClienteExistente(ServicoRequest servicoRequest) {
        verificarCamposObrigatoriosServico(servicoRequest);
        Cliente cliente = verificarExistenciaCliente(servicoRequest.idCliente());
        Tecnico tecnico = verificarExistenciaTecnico(servicoRequest.idTecnico());
        verificarCamposNaoObrigatoriosServico(servicoRequest);

        cadastrarServico(servicoRequest, cliente, tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void verificarCamposObrigatoriosServico(ServicoRequest servicoRequest) {
        if(servicoRequest.idCliente() == null) {
            throw new RuntimeException("Cliente não selecionado");
        }
        if(servicoRequest.idTecnico() == null) {
            throw new RuntimeException("Técnico não selecionado");
        }
        if(StringUtils.isBlank(servicoRequest.equipamento())) {
            throw new RuntimeException("Equipamento é obrigatório");
        }
        if(StringUtils.isBlank(servicoRequest.marca())) {
            throw new RuntimeException("Marca é obrigatória");
        }
        if(StringUtils.isBlank(servicoRequest.descricao())) {
            throw new RuntimeException("Descrição é obrigatória");
        }
        if(servicoRequest.descricao().length() < 10) {
            throw new RuntimeException("Descrição precisa ter pelo menos 10 caracteres");
        }
        if(servicoRequest.descricao().split(" ").length < 2) {
            throw new RuntimeException("Descrição precisa ter pelo menos 3 palavras");
        }
        if(StringUtils.isBlank(servicoRequest.filial())) {
            throw new RuntimeException("A filial é obrigatória");
        }
    }
    private void verificarCamposNaoObrigatoriosServico(ServicoRequest servicoRequest) {
        if(StringUtils.isNotBlank(servicoRequest.dataAtendimento())) {
            convertData(servicoRequest.dataAtendimento());
        }
        if(StringUtils.isNotBlank(servicoRequest.horarioPrevisto()) && (!servicoRequest.horarioPrevisto().equalsIgnoreCase("MANHA") || !servicoRequest.horarioPrevisto().equalsIgnoreCase("TARDE"))) {
            throw new RuntimeException("Horário enviado de forma errada, manha ou tarde");
        }
    }
    private Cliente verificarExistenciaCliente(Integer idCliente) {
        return clienteRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
    private Tecnico verificarExistenciaTecnico(Integer idTecnico) {
        return tecnicoRepository.findById(idTecnico).orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
    }
    private void cadastrarServico(ServicoRequest servicoRequest, Cliente cliente, Tecnico tecnico) {
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
        servicoRepository.save(novoServico);
    }

    public static Date convertData(String data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dataFormatada;
        try{
            dataFormatada = formatter.parse(data);
        } catch (ParseException e){
            throw new RuntimeException("Data em formato errado");
        }
        return dataFormatada;
    }
}