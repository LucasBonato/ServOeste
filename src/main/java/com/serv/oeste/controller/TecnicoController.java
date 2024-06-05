package com.serv.oeste.controller;

import com.serv.oeste.models.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.service.TecnicoService;
import com.serv.oeste.models.dtos.TecnicoDTO;
import com.serv.oeste.models.enums.Situacao;
import com.serv.oeste.models.tecnico.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController {
    @Autowired private TecnicoService tecnicoService;

    @PostMapping("/find")
    public ResponseEntity<List<Tecnico>> getBy(@RequestBody TecnicoRequestFilter filter){
        return tecnicoService.getBy(filter);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> getOne(@PathVariable Integer id){
        return tecnicoService.getOne(id);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TecnicoDTO tecnicoDTO){
        return tecnicoService.create(tecnicoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody TecnicoDTO tecnicoDTO){
        return tecnicoService.update(id, tecnicoDTO);
    }

    @DeleteMapping
    public ResponseEntity desativandoList(@RequestBody List<Integer> ids){
        return tecnicoService.disableAList(ids);
    }
}
