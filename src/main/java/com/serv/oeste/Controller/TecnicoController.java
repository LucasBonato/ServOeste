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
    @GetMapping("/findBy")
    public ResponseEntity<List<Tecnico>> getLikeId(@RequestParam(value = "id") @PathVariable Integer id){
        return tecnicoService.getLikeId(id);
    }
    @GetMapping("/nome")
    public ResponseEntity<List<Tecnico>> getByNomeOrSobrenome(@RequestParam(value = "n") @PathVariable String nomeOuSobrenome){
        return tecnicoService.getByNomeOrSobrenome(nomeOuSobrenome);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TecnicoDTO tecnicoDTO){
        return tecnicoService.create(tecnicoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody TecnicoDTO tecnicoDTO){
        return tecnicoService.update(id, tecnicoDTO);
    }

    @DeleteMapping()
    public ResponseEntity desativandoList(@RequestBody List<Integer> ids){
        return tecnicoService.disableAList(ids);
    }
}
