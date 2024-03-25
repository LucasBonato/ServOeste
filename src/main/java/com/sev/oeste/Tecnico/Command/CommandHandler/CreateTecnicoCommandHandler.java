package com.sev.oeste.Tecnico.Command.CommandHandler;

import com.sev.oeste.Tecnico.Command.Command;
import com.sev.oeste.Tecnico.Command.CommandHandler.Models.BaseCommand;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Tecnico;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateTecnicoCommandHandler extends BaseCommand implements Command<TecnicoDTO, Void> {
    @Override
    public ResponseEntity<Void> execute(TecnicoDTO tecnicoDTO) {
        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));

        verifyFieldsOfTecnico(tecnico);

        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }
}
