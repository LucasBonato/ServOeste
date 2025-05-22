package com.serv.oeste.application.dtos.requests;

public record ClienteServicoRequest(
        ClienteRequest clienteRequest,
        ServicoRequest servicoRequest
) { }
