package com.serv.oeste.models.tecnico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidade {
    private Date data;
    private Integer numeroDiaSemana;
    private String nomeDiaSemana;
    private String periodo;
    private Integer quantidadeServicos;
}
