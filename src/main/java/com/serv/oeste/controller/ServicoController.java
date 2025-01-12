package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.ServicoSwagger;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.ClienteServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.models.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.models.dtos.reponses.TecnicoDisponibilidadeResponse;
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

    @DeleteMapping
    public ResponseEntity<Void> deleteListOfServicesById(@RequestBody List<Integer> ids) {
        return servicoService.deleteListOfServicesById(ids);
    }
}