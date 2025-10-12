package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.presentation.swagger.TecnicoSwagger;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.application.services.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tecnico")
public class TecnicoController implements TecnicoSwagger {
    @Autowired private TechnicianService technicianService;

    @PostMapping("/find")
    public ResponseEntity<PageResponse<TecnicoResponse>> fetchListByFilter(
            @RequestBody TecnicoRequestFilter filter,
            @ModelAttribute PageFilterRequest pageFilter
    ){
        return ResponseEntity.ok(technicianService.fetchListByFilter(filter, pageFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> fetchOneById(@PathVariable Integer id){
        return ResponseEntity.ok(technicianService.fetchOneById(id));
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> fetchListAvailability(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return ResponseEntity.ok(technicianService.fetchListAvailability(tecnicoDisponibilidadeRequest.especialidadeId()));
    }

    @PostMapping
    public ResponseEntity<TecnicoWithSpecialityResponse> create(@Valid @RequestBody TecnicoRequest tecnicoRequest){
        TecnicoWithSpecialityResponse tecnico = technicianService.create(tecnicoRequest);

        return ResponseEntity.created(URI.create("/api/tecnico/" + tecnico.id())).body(tecnico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> update(@PathVariable Integer id, @RequestBody TecnicoRequest tecnicoResponse){
        return ResponseEntity.ok(technicianService.update(id, tecnicoResponse));
    }

    @DeleteMapping
    public ResponseEntity<Void> disableListByIds(@RequestBody List<Integer> ids){
        technicianService.disableListByIds(ids);
        return ResponseEntity.ok().build();
    }
}
