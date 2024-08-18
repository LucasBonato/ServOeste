package com.serv.oeste.controller;

import com.serv.oeste.models.dtos.requests.ClienteServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.servico.TecnicoDisponibilidade;
import com.serv.oeste.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoController {
    @Autowired private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Void> cadastrarComClienteExistente(@RequestBody ServicoRequest servicoRequest) {
        return servicoService.cadastrarComClienteExistente(servicoRequest);
    }

    @PostMapping("/cliente")
    public ResponseEntity<Void> cadastrarComClienteNaoExistente(@RequestBody ClienteServicoRequest clienteServicoRequest) {
        return servicoService.cadastrarComClienteNaoExistente(clienteServicoRequest.clienteRequest(), clienteServicoRequest.servicoRequest());
    }

    @GetMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidade>> getDadosDisponibilidadeTecnicos(@RequestParam(value = "c") String conhecimento) {
        return servicoService.getDadosDisponibilidade(conhecimento);
    }
}
