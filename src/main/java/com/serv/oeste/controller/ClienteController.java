package com.serv.oeste.controller;

import com.serv.oeste.models.dtos.reponses.ClienteResponse;
import com.serv.oeste.models.dtos.requests.ClienteRequest;
import com.serv.oeste.models.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAll() {
        return clienteService.getAll();
    }

    @GetMapping("/find")
    public ResponseEntity<List<ClienteResponse>> getBy(@RequestBody ClienteRequestFilter filter){
        return clienteService.getBy(filter);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ClienteRequest clienteRequest) {
        return clienteService.create(clienteRequest);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestParam(value = "id") Integer id, @RequestBody ClienteRequest clienteRequest) {
        return clienteService.update(id, clienteRequest);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletandoList(@RequestBody List<Integer> ids){
        return clienteService.deletandoAList(ids);
    }
}
