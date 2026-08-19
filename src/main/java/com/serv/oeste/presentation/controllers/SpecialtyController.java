package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.requests.SpecialtyRequest;
import com.serv.oeste.application.services.SpecialtyService;
import com.serv.oeste.presentation.swagger.SpecialtySwagger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class SpecialtyController implements SpecialtySwagger {
    @Autowired private SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponse>> findAll() {
        return ResponseEntity.ok(specialtyService.findAll());
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponse> create(@Valid @RequestBody SpecialtyRequest request) {
        EspecialidadeResponse response = specialtyService.create(request);

        return ResponseEntity.created(URI.create("/api/especialidades/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody SpecialtyRequest request
    ) {
        return ResponseEntity.ok(specialtyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        specialtyService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}