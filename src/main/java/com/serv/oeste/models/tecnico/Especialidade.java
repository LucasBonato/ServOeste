package com.serv.oeste.models.tecnico;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Especialidade")
@Data
@NoArgsConstructor
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

    public Especialidade(Integer id, String conhecimento){
        this.id = id;
        this.conhecimento = conhecimento;
    }
}
