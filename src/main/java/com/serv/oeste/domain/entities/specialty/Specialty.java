package com.serv.oeste.domain.entities.specialty;

public class Specialty {
    private Integer id;
    private String conhecimento;

    public Specialty(Integer id, String conhecimento) {
        this.id = id;
        this.conhecimento = conhecimento;
    }

    public Integer getId() {
        return id;
    }

    public String getConhecimento() {
        return conhecimento;
    }
}
