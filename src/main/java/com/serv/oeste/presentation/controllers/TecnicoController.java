package com.serv.oeste.presentation.controllers;

import com.serv.oeste.presentation.swagger.TecnicoSwagger;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.application.services.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController implements TecnicoSwagger {
    @Autowired private TecnicoService tecnicoService;

    @PostMapping("/find")
    public ResponseEntity<List<TecnicoResponse>> fetchListByFilter(@RequestBody TecnicoRequestFilter filter){
        return tecnicoService.fetchListByFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> fetchOneById(@PathVariable Integer id){
        return tecnicoService.fetchOneById(id);
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> fetchListAvailability(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return tecnicoService.fetchListAvailability(tecnicoDisponibilidadeRequest.especialidadeId());
    }

    @PostMapping
    public ResponseEntity<TecnicoWithSpecialityResponse> create(@RequestBody TecnicoRequest tecnicoRequest){
        return tecnicoService.create(tecnicoRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> update(@PathVariable Integer id, @RequestBody TecnicoRequest tecnicoResponse){
        return tecnicoService.update(id, tecnicoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> disableListByIds(@RequestBody List<Integer> ids){
        return tecnicoService.disableListByIds(ids);
    }
}
