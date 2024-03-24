package com.sev.oeste.Controller;

import com.sev.oeste.Tecnico.Command.CommandHandler.CreateTecnicoCommandHandler;
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
    @GetMapping
    public ResponseEntity<List<Tecnico>> getAllT(){
        return getAllTecnicos.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> getOne(@PathVariable Integer id){
        return getTecnico.execute(id);
    }
    @PostMapping
    public ResponseEntity create(@RequestBody TecnicoDTO tecnicoDTO){
        return createTecnico.execute(tecnicoDTO);
    }
}
