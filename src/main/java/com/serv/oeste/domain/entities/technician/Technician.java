package com.serv.oeste.domain.entities.technician;

import com.serv.oeste.domain.utils.StringUtils;
import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.enums.Situacao;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.technician.TechnicianAlreadyDisabledException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotValidException;
import com.serv.oeste.domain.valueObjects.Phone;

import java.util.List;

public class Technician {
    private Integer id;
    private String nome;
    private String sobrenome;
    private Phone telefoneFixo;
    private Phone telefoneCelular;
    private Situacao situacao = Situacao.ATIVO;
    private List<Specialty> especialidades;

    private Technician(
            Integer id,
            String nome,
            String sobrenome,
            Phone telefoneFixo,
            Phone telefoneCelular,
            Situacao situacao,
            List<Specialty> especialidades
    ) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.situacao = situacao;
        this.especialidades = especialidades;
    }

    private Technician(
            String nome,
            String sobrenome,
            Phone telefoneFixo,
            Phone telefoneCelular,
            List<Specialty> especialidades
    ) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.especialidades = especialidades;

        validate();
    }

    public static Technician restore(
            Integer id,
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            Situacao situacao,
            List<Specialty> especialidades
    ) {
        Phone fixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        Phone celular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;

        return new Technician(
                id,
                nome,
                sobrenome,
                fixo,
                celular,
                situacao,
                especialidades
        );
    }

    public static Technician create(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            List<Specialty> especialidades
    ) {
        Phone fixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        Phone celular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;

        return new Technician(
                nome,
                sobrenome,
                fixo,
                celular,
                especialidades
        );
    }

    public void update(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            Situacao situacao,
            List<Specialty> especialidades
    ) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefoneFixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        this.telefoneCelular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;
        this.situacao = situacao;
        this.especialidades = especialidades;

        validate();
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if (especialidades == null || especialidades.isEmpty())
            errors.add(ErrorFields.CONHECIMENTO, "Técnico precisa possuir no mínimo uma especialidade!");
        if ((telefoneCelular == null || telefoneCelular.isPhoneBlank()) && (telefoneFixo == null || telefoneFixo.isPhoneBlank()))
            errors.add(ErrorFields.TELEFONES, "O técnico precisa ter no mínimo um telefone cadastrado!");
        if (especialidades.isEmpty())
            errors.add(ErrorFields.CONHECIMENTO, "O técnico precisa ter pelo menos uma especialidade");
        if (StringUtils.isBlank(nome))
            errors.add(ErrorFields.NOMESOBRENOME, "O nome é obrigatório");
        if (nome != null && nome.length() < 2)
            errors.add(ErrorFields.NOMESOBRENOME, "O nome precisa ter no minimo 2 caracteres");
        if (StringUtils.isBlank(sobrenome))
            errors.add(ErrorFields.NOMESOBRENOME, "O sobrenome é obrigatório");
        if (sobrenome != null && sobrenome.length() < 2)
            errors.add(ErrorFields.NOMESOBRENOME, "O sobrenome precisa ter no minimo 2 caracteres");

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
        return telefoneFixo != null ? telefoneFixo.getPhone() : null;
    }

    public String getTelefoneCelular() {
        return telefoneCelular != null ? telefoneCelular.getPhone() : null;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public List<Specialty> getEspecialidades() {
        return especialidades;
    }
}
