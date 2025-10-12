package com.serv.oeste.domain.entities.technician;

import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.enums.Situacao;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.technician.TechnicianAlreadyDisabledException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotValidException;

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

    public Technician(Integer id, String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao, List<Specialty> especialidades) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
        this.especialidades = especialidades;

        validate();
    }

    private Technician(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, List<Specialty> especialidades) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.especialidades = especialidades;

        validate();
    }

    public static Technician create(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, List<Specialty> especialidades) {
        return new Technician(
                nome,
                sobrenome,
                telefoneFixo,
                telefoneCelular,
                especialidades
        );
    }

    public void update(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, Situacao situacao, List<Specialty> especialidades) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
        this.especialidades = especialidades;

        validate();
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if (especialidades == null || especialidades.isEmpty()) {
            errors.add(ErrorFields.CONHECIMENTO, "Técnico precisa possuir no mínimo uma especialidade!");
        }

        if ((telefoneCelular == null || telefoneCelular.isBlank()) && (telefoneFixo == null || telefoneFixo.isBlank())) {
            errors.add(ErrorFields.TELEFONES, "O técnico precisa ter no mínimo um telefone cadastrado!");
        }

        errors.throwIfAny(TechnicianNotValidException::new);
    }

    public void disable() {
        if (this.situacao == Situacao.DESATIVADO)
            throw new TechnicianAlreadyDisabledException();
        this.situacao = Situacao.DESATIVADO;
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
}
