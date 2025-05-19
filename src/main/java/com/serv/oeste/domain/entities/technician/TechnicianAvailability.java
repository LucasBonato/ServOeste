package com.serv.oeste.domain.entities.technician;

import java.util.Date;

public class TechnicianAvailability {
    private final Integer id;
    private final String nome;
    private final Date data;
    private final Integer dia;
    private final String periodo;
    private final Integer quantidade;

    public TechnicianAvailability(Integer id, String nome, Date data, Integer dia, String periodo, Integer quantidade) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.dia = dia;
        this.periodo = periodo;
        this.quantidade = quantidade;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Date getData() {
        return data;
    }

    public Integer getDia() {
        return dia;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}
