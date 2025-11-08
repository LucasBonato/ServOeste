package com.serv.oeste.infrastructure.entities.technician;

import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "Tecnico")
@Data
@NoArgsConstructor
public class TechnicianEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Sobrenome", nullable = false, length = 50)
    private String sobrenome;

    @Column(name = "Telefone_Fixo", length = 10)
    private String telefoneFixo;

    @Column(name = "Telefone_Celular", length = 11)
    private String telefoneCelular;

    @Enumerated(EnumType.STRING)
    @Column(name = "Situacao", length = 15)
    private Situacao situacao = Situacao.ATIVO;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "Tecnico_Especialidade",
            joinColumns = @JoinColumn(name = "Id_Tecnico"),
            inverseJoinColumns = @JoinColumn(name = "Id_Especialidade")
    )
    private List<SpecialtyEntity> especialidades;

    public TechnicianEntity(Technician technician) {
        this.id = technician.getId();
        this.nome = technician.getNome();
        this.sobrenome = technician.getSobrenome();
        this.telefoneFixo = technician.getTelefoneFixo();
        this.telefoneCelular = technician.getTelefoneCelular();
        this.situacao = technician.getSituacao();
        this.especialidades = technician.getEspecialidades().stream()
                .map(SpecialtyEntity::new)
                .toList();
    }

    public Technician toTechnician() {
        List<Specialty> specialties = especialidades.stream()
                .map(SpecialtyEntity::toSpecialty)
                .toList();

        return Technician.restore(
                this.id,
                this.nome,
                this.sobrenome,
                this.telefoneFixo,
                this.telefoneCelular,
                this.situacao,
                specialties
        );
    }
}