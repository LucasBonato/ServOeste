package com.serv.oeste.models.servico;

import com.serv.oeste.models.tecnico.Disponibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidade {
    private Integer id;
    private String nome;
    private List<Disponibilidade> disponibilidades;
}
