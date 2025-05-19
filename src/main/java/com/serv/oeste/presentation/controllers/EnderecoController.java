package com.serv.oeste.presentation.controllers;

import com.serv.oeste.presentation.swagger.EnderecoSwagger;
import com.serv.oeste.application.dtos.reponses.EnderecoResponse;
import com.serv.oeste.application.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endereco")
public class EnderecoController implements EnderecoSwagger {
    @Autowired private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<EnderecoResponse> getFieldsEndereco(@RequestParam(value = "cep") String cep){
        return enderecoService.getFields(cep);
    }
}
