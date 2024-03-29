package com.serv.oeste.Controller;

import com.serv.oeste.Tecnico.Command.CommandHandler.CreateTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Command.CommandHandler.DeleteTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.serv.oeste.Tecnico.Command.CommandHandler.UpdateTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Tecnico.Query.QueryHandlers.GetAllTecnicosDesativadosQueryHandler;
import com.serv.oeste.Tecnico.Query.QueryHandlers.GetAllTecnicosLicencaQueryHandler;
import com.serv.oeste.Tecnico.Query.QueryHandlers.GetAllTecnicosQueryHandler;
import com.serv.oeste.Tecnico.Query.QueryHandlers.GetTecnicoQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController {

    @Autowired private GetAllTecnicosQueryHandler getAllTecnicos;
    @Autowired private GetAllTecnicosLicencaQueryHandler getAllLicenca;
    @Autowired private GetAllTecnicosDesativadosQueryHandler getAllDesativados;
    @Autowired private GetTecnicoQueryHandler getTecnico;
    @Autowired private CreateTecnicoCommandHandler createTecnico;
    @Autowired private UpdateTecnicoCommandHandler updateTecnico;
    @Autowired private DeleteTecnicoCommandHandler deleteTecnico;
    @GetMapping
    public ResponseEntity<List<Tecnico>> getAll(){
        return getAllTecnicos.execute(null);
    }
    @GetMapping("/licenca")
    public ResponseEntity<List<Tecnico>> getAllLicenca(){
        return getAllLicenca.execute(null);
    }
    @GetMapping("/desativado")
    public ResponseEntity<List<Tecnico>> getAllDesativados(){
        return getAllDesativados.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> getOne(@PathVariable Integer id){
        return getTecnico.execute(id);
    }
    @PostMapping
    public ResponseEntity create(@RequestBody TecnicoDTO tecnicoDTO){
        return createTecnico.execute(tecnicoDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody TecnicoDTO tecnicoDTO){
        return updateTecnico.execute(new UpdateTecnicoCommand(id, tecnicoDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        return deleteTecnico.execute(id);
    }
}
