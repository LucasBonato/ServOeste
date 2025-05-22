package com.serv.oeste.domain.contracts;

import java.util.Date;

public interface TechnicianAvailabilityProjection {
    Integer getId();
    String getNome();
    Date getData();
    Integer getDia();
    String getPeriodo();
    Integer getQuantidade();
}
