package com.serv.oeste.models.servico;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidade {
    @Id
    private Integer id;
    private Integer data;
    private String dia;
    private String periodo;
    private String nome;
    private Integer quantidade;
}
