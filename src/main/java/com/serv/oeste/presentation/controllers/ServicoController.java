package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.presentation.swagger.ServicoSwagger;
import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoController implements ServicoSwagger {
    @Autowired private ServiceService serviceService;

    @PostMapping("/find")
    public ResponseEntity<PageResponse<ServicoResponse>> fetchListByFilter(
            @RequestBody ServicoRequestFilter servicoRequestFilter,
            @ModelAttribute PageFilterRequest pageFilter
    ) {
        return serviceService.fetchListByFilter(servicoRequestFilter, pageFilter);
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> cadastrarComClienteExistente(@RequestBody ServicoRequest servicoRequest) {
        return serviceService.cadastrarComClienteExistente(servicoRequest);
    }

    @PostMapping("/cliente")
    public ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(@RequestBody ClienteServicoRequest clienteServicoRequest) {
        return serviceService.cadastrarComClienteNaoExistente(clienteServicoRequest.clienteRequest(), clienteServicoRequest.servicoRequest());
    }

    @PutMapping
    public ResponseEntity<ServicoResponse> update(@RequestParam(value = "id") Integer id, @RequestBody ServicoUpdateRequest servicoUpdateRequest) {
        return serviceService.update(id, servicoUpdateRequest);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListById(@RequestBody List<Integer> ids) {
        return serviceService.deleteListByIds(ids);
    }
}