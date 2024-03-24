package com.sev.oeste.Tecnico.Command.CommandHandler;

import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Command.Command;
import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteTecnicoCommandHandler implements Command<Integer, Void> {
    @Autowired private TecnicoRepository tecnicoRepository;

    @Override
    public ResponseEntity<Void> execute(Integer id) {
        verifyIfTheTecnicoExists(id);

        Tecnico tecnico = tecnicoRepository.findById(id).get();
        tecnico.setSituacao(Situacao.DESATIVADO);

        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    private void verifyIfTheTecnicoExists(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new RuntimeException();
        }
    }
}
