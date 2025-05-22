package com.serv.oeste.application.dtos.reponses;

import com.serv.oeste.domain.entities.technician.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidadeResponse {
    private Integer id;
    private String nome;
    private Integer quantidadeTotalServicos;
    private List<Availability> disponibilidades;
}
