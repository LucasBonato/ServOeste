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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return technicianService.fetchListByFilter(filter, pageFilter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> fetchOneById(@PathVariable Integer id){
        return technicianService.fetchOneById(id);
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> fetchListAvailability(@RequestBody TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest) {
        return technicianService.fetchListAvailability(tecnicoDisponibilidadeRequest.especialidadeId());
    }

    @PostMapping
    public ResponseEntity<TecnicoWithSpecialityResponse> create(@RequestBody TecnicoRequest tecnicoRequest){
        return technicianService.create(tecnicoRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoWithSpecialityResponse> update(@PathVariable Integer id, @RequestBody TecnicoRequest tecnicoResponse){
        return technicianService.update(id, tecnicoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> disableListByIds(@RequestBody List<Integer> ids){
        return technicianService.disableListByIds(ids);
    }
}
