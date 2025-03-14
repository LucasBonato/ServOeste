package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.ServicoSwagger;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.*;
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
    public ResponseEntity<List<ServicoResponse>> fetchListByFilter(@RequestBody ServicoRequestFilter servicoRequestFilter) {
        return servicoService.fetchListByFilter(servicoRequestFilter);
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(@RequestBody ServicoRequest servicoRequest) {
        return servicoService.cadastrarComClienteExistente(servicoRequest);
    }

    @PostMapping("/cliente")
    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(@RequestBody ClienteServicoRequest clienteServicoRequest) {
        return servicoService.cadastrarComClienteNaoExistente(clienteServicoRequest.clienteRequest(), clienteServicoRequest.servicoRequest());
    }

    @PutMapping
    public ResponseEntity<ServicoResponse> update(@RequestParam(value = "id") Integer id, @RequestBody ServicoUpdateRequest servicoUpdateRequest) {
        return servicoService.update(id, servicoUpdateRequest);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListById(@RequestBody List<Integer> ids) {
        return servicoService.deleteListByIds(ids);
    }
}