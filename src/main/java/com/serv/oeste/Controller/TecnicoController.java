package com.serv.oeste.Controller;

import com.serv.oeste.Service.TecnicoService;

import com.serv.oeste.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Models.Situacao;
import com.serv.oeste.Models.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController {

    @Autowired private TecnicoService tecnicoService;
    @GetMapping
    public ResponseEntity<List<Tecnico>> getAll(){
        return tecnicoService.getAllBySituacao(Situacao.ATIVO);
    }
    @GetMapping("/licenca")
    public ResponseEntity<List<Tecnico>> getAllLicenca(){
        return tecnicoService.getAllBySituacao(Situacao.LICENCA);
    }
    @GetMapping("/desativado")
    public ResponseEntity<List<Tecnico>> getAllDesativados(){
        return tecnicoService.getAllBySituacao(Situacao.DESATIVADO);
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
    @DeleteMapping("/{id}")
    public ResponseEntity desativando(@PathVariable Integer id){
        return tecnicoService.disabled(id);
    }
}
