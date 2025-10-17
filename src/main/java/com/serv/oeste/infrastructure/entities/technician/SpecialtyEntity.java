package com.serv.oeste.infrastructure.entities.technician;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serv.oeste.domain.valueObjects.Specialty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Especialidade")
@Data
@NoArgsConstructor
public class SpecialtyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Conhecimento")
    private String conhecimento;

    @JsonIgnore
    @ManyToMany(mappedBy = "especialidades")
    private List<TechnicianEntity> tecnicos;

    public SpecialtyEntity(Specialty specialty) {
        this.id = specialty.id();
        this.conhecimento = specialty.conhecimento();
    }

    public Specialty toSpecialty() {
        return new Specialty(
                this.id,
                this.conhecimento
        );
    }
}
