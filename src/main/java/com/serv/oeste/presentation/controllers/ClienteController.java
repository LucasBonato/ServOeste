package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.presentation.swagger.ClienteSwagger;
import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController implements ClienteSwagger {
    @Autowired private ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> fetchOneById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.fetchOneById(id));
    }

    @PostMapping("/find")
    public ResponseEntity<PageResponse<ClienteResponse>> fetchListByFilter(
            @RequestBody ClienteRequestFilter filter,
            @ModelAttribute PageFilterRequest pageFilter
    ){
        return ResponseEntity.ok(clientService.fetchListByFilter(filter, pageFilter));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse cliente = clientService.create(clienteRequest);

        return ResponseEntity
                .created(URI.create("/api/cliente/" + cliente.id()))
                .body(cliente);
    }

    @PutMapping
    public ResponseEntity<ClienteResponse> update(@RequestParam(value = "id") Integer id, @Valid @RequestBody ClienteRequest clienteRequest) {
        return ResponseEntity.ok(clientService.update(id, clienteRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListByIds(@RequestBody List<Integer> ids){
        clientService.deleteListByIds(ids);
        return ResponseEntity.ok().build();
    }
}
