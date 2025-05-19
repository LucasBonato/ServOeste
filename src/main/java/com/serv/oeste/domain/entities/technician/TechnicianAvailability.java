package com.serv.oeste.domain.entities.technician;

import java.util.Date;

public class TechnicianAvailability {
    private Integer id;
    private String nome;
    private Date data;
    private Integer dia;
    private String periodo;
    private Integer quantidade;

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
