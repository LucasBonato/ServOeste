package com.serv.oeste.Models.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TecnicoDTO {
    private String nome;
    private String sobrenome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String situacao;
    private List<Integer> especialidades_Ids;

    public TecnicoDTO(List<Integer> especialidades_Ids){
        this.especialidades_Ids = especialidades_Ids;
    }
}
