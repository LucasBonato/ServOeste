package com.serv.oeste.Tecnico.Models.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class TecnicoDTO {
    private String nome;
    private String sobrenome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String situacao;
    private List<Integer> especialidades_Ids;
}
