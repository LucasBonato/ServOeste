package com.serv.oeste.domain.valueObjects;

public record TechnicianFilter(
        String id,
        String nome,
        String situacao,
        String equipamento,
        String telefone
) { }
