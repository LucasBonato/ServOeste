package com.serv.oeste.models.dtos.reponses;

import com.serv.oeste.models.enums.Situacao;
import com.serv.oeste.models.tecnico.Tecnico;

public record TecnicoResponse(
        Integer id,
        String nome,
        String sobrenome,
        String telefoneFixo,
        String telefoneCelular,
        Situacao situacao
) {
    public TecnicoResponse(Tecnico tecnico) {
        this(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getSobrenome(),
                tecnico.getTelefoneFixo(),
                tecnico.getTelefoneCelular(),
                tecnico.getSituacao()
        );
    }
}
