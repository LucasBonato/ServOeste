package com.sev.oeste.Tecnico.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Tecnico")
@Data
public class Tecnico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Sobrenome", nullable = false, length = 50)
    private String sobrenome;

    @Column(name = "Telefone_Fixo", length = 11)
    private String telefoneFixo;

    @Column(name = "Telefone_Celular", length = 11)
    private String telefoneCelular;

    @Enumerated(EnumType.STRING)
    @Column(name = "Situacao", length = 15)
    private Situacao situacao = Situacao.ATIVO;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Tecnico_Especialidade", joinColumns = @JoinColumn(name = "Id_Tecnico"), inverseJoinColumns = @JoinColumn(name = "Id_Especialidade"))
    private List<Especialidade> especialidades;
}
