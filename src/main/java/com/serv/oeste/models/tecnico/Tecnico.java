package com.serv.oeste.models.tecnico;

import com.serv.oeste.models.dtos.TecnicoDTO;
import com.serv.oeste.models.enums.Situacao;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Tecnico")
@Data
@NoArgsConstructor
public class Tecnico {
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Tecnico_Especialidade", joinColumns = @JoinColumn(name = "Id_Tecnico"), inverseJoinColumns = @JoinColumn(name = "Id_Especialidade"))
    private List<Especialidade> especialidades;

    public Tecnico(TecnicoDTO tecnico){
        this.nome = tecnico.getNome();
        this.sobrenome = tecnico.getSobrenome();
        this.telefoneFixo = tecnico.getTelefoneFixo();
        this.telefoneCelular = tecnico.getTelefoneCelular();
    }

    public Tecnico(Integer id, String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao){
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
    }
}
