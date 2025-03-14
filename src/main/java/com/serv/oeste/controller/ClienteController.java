package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.ClienteSwagger;
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
public class ClienteController implements ClienteSwagger {
    @Autowired private ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> fetchOneById(@PathVariable Integer id) {
        return clienteService.fetchOneById(id);
    }

    @PostMapping("/find")
    public ResponseEntity<List<ClienteResponse>> fetchListByFilter(@RequestBody ClienteRequestFilter filter){
        return clienteService.fetchListByFilter(filter);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@RequestBody ClienteRequest clienteRequest) {
        return clienteService.create(clienteRequest);
    }

    @PutMapping
    public ResponseEntity<ClienteResponse> update(@RequestParam(value = "id") Integer id, @RequestBody ClienteRequest clienteRequest) {
        return clienteService.update(id, clienteRequest);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListByIds(@RequestBody List<Integer> ids){
        return clienteService.deleteListByIds(ids);
    }
}
