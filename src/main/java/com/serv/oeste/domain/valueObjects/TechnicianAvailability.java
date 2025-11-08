package com.serv.oeste.domain.valueObjects;

import java.time.LocalDate;

public record TechnicianAvailability(
        Integer id,
        String nome,
        LocalDate data,
        Integer dia,
        String periodo,
        Integer quantidade
) { }
