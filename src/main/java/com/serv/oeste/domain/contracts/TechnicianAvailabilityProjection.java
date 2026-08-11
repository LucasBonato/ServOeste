package com.serv.oeste.domain.contracts;

import java.time.LocalDateTime;

public interface TechnicianAvailabilityProjection {
    Integer getId();
    String getNome();
    LocalDateTime getData();
    Integer getDia();
    String getPeriodo();
    Integer getQuantidade();
}
