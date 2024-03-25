package com.sev.oeste.Tecnico.Command.CommandHandler;

import com.sev.oeste.Tecnico.Command.Command;
import com.sev.oeste.Tecnico.Command.CommandHandler.Models.BaseCommand;
import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteTecnicoCommandHandler extends BaseCommand implements Command<Integer, Void> {
    @Override
    public ResponseEntity<Void> execute(Integer id) {
        verifyIfTecnicoExists(id);

        Tecnico tecnico = tecnicoRepository.findById(id).get();
        tecnico.setSituacao(Situacao.DESATIVADO);

        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }
}
