package com.serv.oeste.models.tecnico;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidade {
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Nome")
    private String nome;

    @Column(name = "Data")
    private Date data;

    @Column(name = "Dia")
    private Integer dia;

    @Column(name = "Periodo")
    private String periodo;

    @Column(name = "Quantidade")
    private Integer quantidade;

    public TecnicoDisponibilidade(TecnicoDisponibilidadeProjection tecnicoDisponibilidadeProjection) {
        this.id = tecnicoDisponibilidadeProjection.getId();
        this.nome = tecnicoDisponibilidadeProjection.getNome();
        this.data = tecnicoDisponibilidadeProjection.getData();
        this.dia = tecnicoDisponibilidadeProjection.getDia();
        this.periodo = tecnicoDisponibilidadeProjection.getPeriodo();
        this.quantidade = tecnicoDisponibilidadeProjection.getQuantidade();
    }
}

