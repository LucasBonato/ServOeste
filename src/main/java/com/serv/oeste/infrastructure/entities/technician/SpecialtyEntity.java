package com.serv.oeste.infrastructure.entities.technician;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serv.oeste.domain.valueObjects.Specialty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "especialidade")
@Data
@NoArgsConstructor
public class SpecialtyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Conhecimento", nullable = false, unique = true)
    private String conhecimento;

    @Column(name = "Ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "Criado_Em")
    private LocalDateTime criadoEm;

    @Column(name = "Atualizado_Em")
    private LocalDateTime atualizadoEm;

    @JsonIgnore
    @ManyToMany(mappedBy = "especialidades")
    private List<TechnicianEntity> tecnicos;

    public SpecialtyEntity(Specialty specialty) {
        this.id = specialty.id();
        this.conhecimento = specialty.conhecimento();
        this.ativo = specialty.isAtivo();
    }

    public Specialty toDomain() {
        return Specialty.restore(
                this.id,
                this.conhecimento,
                this.ativo
        );
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.criadoEm = now;
        this.atualizadoEm = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}