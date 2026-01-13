package com.serv.oeste.application.contracts.clients;

import com.serv.oeste.application.dtos.reponses.ViaCepResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/ws")
public interface ViaCepClient {

    @GetExchange("/{cep}/json")
    ViaCepResponse getCep(@PathVariable("cep") String cep);
}
