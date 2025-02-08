package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.TecnicoSwagger;
import com.serv.oeste.models.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.models.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.models.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.service.TecnicoService;
import com.serv.oeste.models.dtos.reponses.TecnicoRequest;
import com.serv.oeste.models.tecnico.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController implements TecnicoSwagger {
    @Autowired private TecnicoService tecnicoService;

    @PostMapping("/find")
    public ResponseEntity<List<Tecnico>> getBy(@RequestBody TecnicoRequestFilter filter){
        return tecnicoService.getBy(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> getOne(@PathVariable Integer id){
        return tecnicoService.getOne(id);
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> getDadosDisponibilidadeTecnicos(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return tecnicoService.getDadosDisponibilidade(tecnicoDisponibilidadeRequest.especialidadeId());
    }

    @PostMapping
    public ResponseEntity<Tecnico> create(@RequestBody TecnicoRequest tecnicoRequest){
        return tecnicoService.create(tecnicoRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> update(@PathVariable Integer id, @RequestBody TecnicoRequest tecnicoResponse){
        return tecnicoService.update(id, tecnicoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> desativandoList(@RequestBody List<Integer> ids){
        return tecnicoService.disableAList(ids);
    }
}
