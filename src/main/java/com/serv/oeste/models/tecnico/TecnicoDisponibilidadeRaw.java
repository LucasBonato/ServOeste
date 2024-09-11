package com.serv.oeste.models.tecnico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidadeRaw {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Nome")
    private String nome;

    @Column(name = "Data")
    private Integer data;

    @Column(name = "Dia")
    private String dia;

    @Column(name = "Periodo")
    private String periodo;

    @Column(name = "Quantidade")
    private Integer quantidade;
}
