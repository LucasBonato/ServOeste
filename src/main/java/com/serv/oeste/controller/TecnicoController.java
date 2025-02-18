package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.TecnicoSwagger;
import com.serv.oeste.models.dtos.reponses.TecnicoAllResponse;
import com.serv.oeste.models.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.models.dtos.reponses.TecnicoResponse;
import com.serv.oeste.models.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.models.dtos.requests.TecnicoRequest;
import com.serv.oeste.models.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController implements TecnicoSwagger {
    @Autowired private TecnicoService tecnicoService;

    @PostMapping("/find")
    public ResponseEntity<List<TecnicoResponse>> getBy(@RequestBody TecnicoRequestFilter filter){
        return tecnicoService.getBy(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoAllResponse> getOne(@PathVariable Integer id){
        return tecnicoService.getOne(id);
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> getDadosDisponibilidadeTecnicos(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return tecnicoService.getDadosDisponibilidade(tecnicoDisponibilidadeRequest.especialidadeId());
    }

    @PostMapping
    public ResponseEntity<TecnicoAllResponse> create(@RequestBody TecnicoRequest tecnicoRequest){
        return tecnicoService.create(tecnicoRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoAllResponse> update(@PathVariable Integer id, @RequestBody TecnicoRequest tecnicoResponse){
        return tecnicoService.update(id, tecnicoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> disableAList(@RequestBody List<Integer> ids){
        return tecnicoService.disableAList(ids);
    }
}
