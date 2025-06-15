package com.serv.oeste.domain.entities.technician;

import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.domain.enums.Situacao;
import java.util.List;

public class Technician {
    private Integer id;
    private String nome;
    private String sobrenome;
    private String telefoneFixo;
    private String telefoneCelular;
    private Situacao situacao = Situacao.ATIVO;
    private List<Specialty> especialidades;

    public Technician() {}

    public Technician(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
    }

    public Technician(Integer id, String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao, List<Specialty> especialidades) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
        this.especialidades = especialidades;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public List<Specialty> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Specialty> especialidades) {
        this.especialidades = especialidades;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public void setAll(Integer id, String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao, List<Specialty> especialidades) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
        this.especialidades = especialidades;
    }
}
