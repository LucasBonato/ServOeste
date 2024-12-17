package com.serv.oeste.models.tecnico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Entity
@Table(name = "tecnico_disponibilidade")
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDisponibilidade {
    @Id
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
}
