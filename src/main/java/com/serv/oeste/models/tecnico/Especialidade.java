package com.serv.oeste.models.tecnico;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;
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

    public Especialidade(String conhecimento){
        this.conhecimento = conhecimento;
    }
}
