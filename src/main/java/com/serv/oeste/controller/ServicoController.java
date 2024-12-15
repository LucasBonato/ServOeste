package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.ServicoSwagger;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.ClienteServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.models.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.models.servico.TecnicoDisponibilidade;
import com.serv.oeste.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoController implements ServicoSwagger {
    @Autowired private ServicoService servicoService;

    @PostMapping("/find")
    public ResponseEntity<List<ServicoResponse>> getByFilter(@RequestBody ServicoRequestFilter servicoRequestFilter) {
        return servicoService.getByFilter(servicoRequestFilter);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarComClienteExistente(@RequestBody ServicoRequest servicoRequest) {
        return servicoService.cadastrarComClienteExistente(servicoRequest);
    }

    @PostMapping("/cliente")
    public ResponseEntity<Void> cadastrarComClienteNaoExistente(@RequestBody ClienteServicoRequest clienteServicoRequest) {
        return servicoService.cadastrarComClienteNaoExistente(clienteServicoRequest.clienteRequest(), clienteServicoRequest.servicoRequest());
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidade>> getDadosDisponibilidadeTecnicos(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return servicoService.getDadosDisponibilidade(tecnicoDisponibilidadeRequest.especialidadeId());
    }
}