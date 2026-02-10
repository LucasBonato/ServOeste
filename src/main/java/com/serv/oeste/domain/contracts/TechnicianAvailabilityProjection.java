package com.serv.oeste.domain.contracts;

import java.time.LocalDate;

public interface TechnicianAvailabilityProjection {
    Integer getId();
    String getNome();
    LocalDate getData();
    Integer getDia();
    String getPeriodo();
    Integer getQuantidade();
}
