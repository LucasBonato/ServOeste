package com.serv.oeste.models.dtos.requests;

public record ClienteServicoRequest(
        ClienteRequest clienteRequest,
        ServicoRequest servicoRequest
) { }
