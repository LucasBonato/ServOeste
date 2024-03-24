package com.sev.oeste.Controller;

import com.sev.oeste.Tecnico.Command.CommandHandler.CreateTecnicoCommandHandler;
import com.sev.oeste.Tecnico.Command.CommandHandler.DeleteTecnicoCommandHandler;
import com.sev.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.sev.oeste.Tecnico.Command.CommandHandler.UpdateTecnicoCommandHandler;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Tecnico;
import com.sev.oeste.Tecnico.Query.QueryHandlers.GetAllTecnicosQueryHandler;
import com.sev.oeste.Tecnico.Query.QueryHandlers.GetTecnicoQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController {

    @Autowired private GetAllTecnicosQueryHandler getAllTecnicos;
    @Autowired private GetTecnicoQueryHandler getTecnico;
    @Autowired private CreateTecnicoCommandHandler createTecnico;
    @Autowired private UpdateTecnicoCommandHandler updateTecnico;
    @Autowired private DeleteTecnicoCommandHandler deleteTecnico;
    @GetMapping
    public ResponseEntity<List<Tecnico>> getAll(){
        return getAllTecnicos.execute(null);
    }
    @GetMapping("/licenca")
    public ResponseEntity<Tecnico> getAllLicenca(@PathVariable Integer id){
        return getAllLicenca.execute();
    }
    @GetMapping("/desativado")
    public ResponseEntity<Tecnico> getAllDesativados(@PathVariable Integer id){
        return getAllDesativados.execute(id);
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
