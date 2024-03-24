package com.sev.oeste.Tecnico.Command.CommandHandler;

import com.sev.oeste.Repository.EspecialidadeRepository;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Command.Command;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Especialidade;
import com.sev.oeste.Tecnico.Models.Tecnico;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTecnicoCommandHandler implements Command<TecnicoDTO, Void> {

    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private EspecialidadeRepository especialidadeRepository;

    @Override
    public ResponseEntity<Void> execute(TecnicoDTO tecnicoDTO) {
        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(setEspecialidadeId(tecnicoDTO));
        verificationTecnico(tecnico);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    private void verificationTecnico(Tecnico tecnico){
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

    private List<Especialidade> setEspecialidadeId(TecnicoDTO tecnico){
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
}
