package com.sev.oeste.Tecnico.Command.CommandHandler.Models;

import com.sev.oeste.Repository.EspecialidadeRepository;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Especialidade;
import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseCommand {
    @Autowired protected TecnicoRepository tecnicoRepository;
    @Autowired protected EspecialidadeRepository especialidadeRepository;

    protected Situacao getSituacaoTecnico(TecnicoDTO tecnicoDTO) {
        switch (tecnicoDTO.getSituacao()){
            case "ativo" -> { return Situacao.ATIVO;}
            case "licença" -> { return Situacao.LICENCA;}
            case "desativado" -> { return Situacao.DESATIVADO;}
        }
        throw new RuntimeException();
    }

    protected List<Especialidade> getEspecialidadesTecnico(TecnicoDTO tecnico){
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

    protected void verifyIfTecnicoExists(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new RuntimeException();
        }
    }

    protected void verifyFieldsOfTecnico(Tecnico tecnico){
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
