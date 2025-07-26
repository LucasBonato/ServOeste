package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.presentation.swagger.ClienteSwagger;
import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController implements ClienteSwagger {
    @Autowired private ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> fetchOneById(@PathVariable Integer id) {
        return clientService.fetchOneById(id);
    }

    @PostMapping("/find")
    public ResponseEntity<PageResponse<ClienteResponse>> fetchListByFilter(
            @RequestBody ClienteRequestFilter filter,
            @ModelAttribute PageFilterRequest pageFilter
    ){
        return clientService.fetchListByFilter(filter, pageFilter);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@RequestBody ClienteRequest clienteRequest) {
        return clientService.create(clienteRequest);
    }

    @PutMapping
    public ResponseEntity<ClienteResponse> update(@RequestParam(value = "id") Integer id, @RequestBody ClienteRequest clienteRequest) {
        return clientService.update(id, clienteRequest);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteListByIds(@RequestBody List<Integer> ids){
        return clientService.deleteListByIds(ids);
    }
}
