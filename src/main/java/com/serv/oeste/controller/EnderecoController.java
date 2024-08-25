package com.serv.oeste.controller;

import com.serv.oeste.configuration.swagger.EnderecoSwagger;
import com.serv.oeste.models.viacep.ViaCepDTO;
import com.serv.oeste.service.EnderecoService;
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
    public ResponseEntity<ViaCepDTO> getFieldsEndereco(@RequestParam(value = "cep") String cep){
        return enderecoService.getFields(cep);
    }
}
