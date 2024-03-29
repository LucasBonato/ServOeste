package com.serv.oeste.Tecnico.Command.CommandHandler;

import com.serv.oeste.Tecnico.Command.CommandHandler.Models.BaseCommand;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Tecnico.Command.Command;
import com.serv.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateTecnicoCommandHandler extends BaseCommand implements Command<UpdateTecnicoCommand, Void> {

    @Override
    public ResponseEntity<Void> execute(UpdateTecnicoCommand updateTecnicoCommand) {
        Integer id = updateTecnicoCommand.getId();
        TecnicoDTO tecnicoDTO = updateTecnicoCommand.getTecnicoDTO();

        verifyIfTecnicoExists(id);

        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));
        tecnico.setSituacao(getSituacaoTecnico(tecnicoDTO));

        verifyFieldsOfTecnico(tecnico);

        tecnico.setId(id);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }
}
