package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.application.services.ClientService;
import com.serv.oeste.application.services.ServiceService;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.presentation.swagger.ServicoSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servico")
public class ServicoController implements ServicoSwagger {
    private final ServiceService serviceService;
    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> fetchOneById(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceService.fetchOneById(id));
    }

    @PostMapping("/find")
    public ResponseEntity<PageResponse<ServicoResponse>> fetchListByFilter(
            @RequestBody ServicoRequestFilter servicoRequestFilter,
            @ModelAttribute PageFilterRequest pageFilter
    ) {
        return ResponseEntity.ok(serviceService.fetchListByFilter(servicoRequestFilter, pageFilter));
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(@Valid @RequestBody ServicoRequest servicoRequest) {
        ServicoResponse servico = serviceService.create(servicoRequest, servicoRequest.idCliente());
        return ResponseEntity
                .created(URI.create("/api/servico/" + servico.id()))
                .body(servico);
    }

    @PostMapping("/cliente")
    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(@Valid @RequestBody ClienteServicoRequest clienteServicoRequest) {
        ClienteResponse cliente = clientService.create(clienteServicoRequest.clienteRequest());
        ServicoResponse servico = serviceService.create(clienteServicoRequest.servicoRequest(), cliente.id());

        return ResponseEntity
                .created(URI.create("/api/servico/" + servico.id()))
                .body(servico);
    }

    @PutMapping
    public ResponseEntity<ServicoResponse> update(@RequestParam(value = "id") Integer id, @Valid @RequestBody ServicoUpdateRequest servicoUpdateRequest) {
        serviceService.update(id, servicoUpdateRequest);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListById(@RequestBody List<Integer> ids) {
        serviceService.deleteListByIds(ids);
        return ResponseEntity
                .ok()
                .build();
    }
}