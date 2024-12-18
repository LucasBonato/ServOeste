package com.serv.oeste.models.tecnico;

import java.util.Date;

public interface TecnicoDisponibilidadeProjection {
    Integer getId();
    String getNome();
    Date getData();
    Integer getDia();
    String getPeriodo();
    Integer getQuantidade();
}
