package com.serv.oeste.models.dtos.reponses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TecnicoResponse {
    private String nome;
    private String sobrenome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String situacao;
    private List<Integer> especialidades_Ids;
}