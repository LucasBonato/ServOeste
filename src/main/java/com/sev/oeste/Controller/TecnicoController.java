package com.sev.oeste.Controller;

import com.sev.oeste.Tecnico.Models.Tecnico;
import com.sev.oeste.Tecnico.Query.QueryHandlers.GetAllTecnicosQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController {

    @Autowired private GetAllTecnicosQueryHandler getAllTecnicos;
    @GetMapping
    public ResponseEntity<List<Tecnico>> getAllTecnicos(){
        return getAllTecnicos.execute(null);
    }
}
