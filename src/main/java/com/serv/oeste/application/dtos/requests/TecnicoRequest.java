package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TecnicoRequest {
    private String nome;
    private String sobrenome;
    private String telefoneFixo;
    private String telefoneCelular;
    private Situacao situacao;
    private List<Integer> especialidades_Ids;

    public Technician toTechnician() {
        return new Technician(
                this.nome,
                this.sobrenome,
                this.telefoneFixo,
                this.telefoneCelular,
                this.situacao
        );
    }
}
