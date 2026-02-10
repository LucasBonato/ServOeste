package com.serv.oeste.domain.entities.technician;

import java.time.LocalDate;

public class Availability {
    private LocalDate data;
    private Integer numeroDiaSemana;
    private String nomeDiaSemana;
    private String periodo;
    private Integer quantidadeServicos;

    public Availability(
            LocalDate data,
            Integer numeroDiaSemana,
            String nomeDiaSemana,
            String periodo,
            Integer quantidadeServicos
    ) {
        this.data = data;
        this.numeroDiaSemana = numeroDiaSemana;
        this.nomeDiaSemana = nomeDiaSemana;
        this.periodo = periodo;
        this.quantidadeServicos = quantidadeServicos;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getNumeroDiaSemana() {
        return numeroDiaSemana;
    }

    public void setNumeroDiaSemana(Integer numeroDiaSemana) {
        this.numeroDiaSemana = numeroDiaSemana;
    }

    public String getNomeDiaSemana() {
        return nomeDiaSemana;
    }

    public void setNomeDiaSemana(String nomeDiaSemana) {
        this.nomeDiaSemana = nomeDiaSemana;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public void setQuantidadeServicos(Integer quantidadeServicos) {
        this.quantidadeServicos = quantidadeServicos;
    }
}
