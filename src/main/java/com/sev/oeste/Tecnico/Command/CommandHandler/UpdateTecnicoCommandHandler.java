package com.sev.oeste.Tecnico.Command.CommandHandler;

import com.sev.oeste.Repository.EspecialidadeRepository;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Command.Command;
import com.sev.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Especialidade;
import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateTecnicoCommandHandler implements Command<UpdateTecnicoCommand, Void> {

    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private EspecialidadeRepository especialidadeRepository;

    @Override
    public ResponseEntity<Void> execute(UpdateTecnicoCommand updateTecnicoCommand) {
        Integer id = updateTecnicoCommand.getId();
        TecnicoDTO tecnicoDTO = updateTecnicoCommand.getTecnicoDTO();

        verifyIfTheTecnicoExists(id);

        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(getEspecialidades(tecnicoDTO));
        tecnico.setSituacao(getSituacao(tecnicoDTO));

        verifyTecnico(tecnico);

        tecnico.setId(id);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    private Situacao getSituacao(TecnicoDTO tecnicoDTO) {
        switch (tecnicoDTO.getSituacao()){
            case "ativo" -> { return Situacao.ATIVO;}
            case "licença" -> { return Situacao.LICENCA;}
            case "desativado" -> { return Situacao.DESATIVADO;}
        }
        throw new RuntimeException();
    }

    private void verifyIfTheTecnicoExists(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new RuntimeException();
        }
    }

    private List<Especialidade> getEspecialidades(TecnicoDTO tecnico){
        if(tecnico.getEspecialidades_Ids().isEmpty()){
            throw new RuntimeException();
        }
        List<Especialidade> especialidades = new ArrayList<>();
        for (Integer id : tecnico.getEspecialidades_Ids()) {
            Optional<Especialidade> especialidadeOptional = especialidadeRepository.findById(id);
            if (especialidadeOptional.isEmpty()){
                throw new RuntimeException();
            }
            especialidades.add(especialidadeOptional.get());
        }
        return especialidades;
    }

    private void verifyTecnico(Tecnico tecnico){
        if(StringUtils.isBlank(tecnico.getNome())) {
            throw new RuntimeException();
        }
        if(StringUtils.isBlank(tecnico.getSobrenome())) {
            throw new RuntimeException();
        }
        if(tecnico.getEspecialidades().isEmpty()) {
            throw new RuntimeException();
        }
        if(tecnico.getTelefoneCelular().isBlank() && tecnico.getTelefoneFixo().isBlank()) {
            throw new RuntimeException();
        }
        if((!tecnico.getTelefoneCelular().isEmpty() && tecnico.getTelefoneCelular().length() < 11) || (!tecnico.getTelefoneFixo().isEmpty() && tecnico.getTelefoneFixo().length() < 11)) {
            throw new RuntimeException();
        }
    }
}
