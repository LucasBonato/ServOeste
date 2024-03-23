package com.sev.oeste.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Especialidade")
@Data
public class Especialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Conhecimento")
    private String conhecimento;

    @ManyToMany(mappedBy = "especialidades")
    @JsonIgnore
    private List<Tecnico> tecnicos;
}
