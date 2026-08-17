package com.serv.oeste.domain.valueObjects;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyNotValidException;
import com.serv.oeste.domain.utils.StringUtils;

public class Specialty {

    public static final String OUTROS = "Outros";

    private final Integer id;
    private String conhecimento;
    private boolean ativo;

    public Specialty(Integer id, String conhecimento) {
        this(id, conhecimento, true);
    }

    private Specialty(Integer id, String conhecimento, boolean ativo) {
        this.id = id;
        this.conhecimento = conhecimento;
        this.ativo = ativo;
        validate();
    }

    public static Specialty restore(Integer id, String conhecimento, boolean ativo) {
        return new Specialty(id, conhecimento, ativo);
    }

    public static Specialty create(String conhecimento) {
        return new Specialty(null, conhecimento, true);
    }

    public void rename(String novoConhecimento) {
        this.conhecimento = novoConhecimento;
        validate();
    }

    public void activate() {
        this.ativo = true;
    }

    public void deactivate() {
        this.ativo = false;
    }

    public boolean isOutros() {
        return OUTROS.equalsIgnoreCase(this.conhecimento);
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if (StringUtils.isBlank(conhecimento))
            errors.add(ErrorFields.CONHECIMENTO, "A especialidade precisa de um nome");
        if (conhecimento.length() < 2)
            errors.add(ErrorFields.CONHECIMENTO, "A especialidade precisa ter no mínimo 2 caracteres");

        errors.throwIfAny(SpecialtyNotValidException::new);
    }

    public Integer getId() {
        return id;
    }

    public String getConhecimento() {
        return conhecimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Integer id() {
        return id;
    }

    public String conhecimento() {
        return conhecimento;
    }
}