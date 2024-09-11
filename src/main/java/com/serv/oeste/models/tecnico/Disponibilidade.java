package com.serv.oeste.models.tecnico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidade {
    private Integer data;
    private String dia;
    private String periodo;
    private Integer quantidade;
}
