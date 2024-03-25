package com.sev.oeste.Tecnico.Command.CommandHandler.Models;

import com.sev.oeste.Exception.Tecnico.*;
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
    private final int MINIMO_DE_CARACTERES_ACEITO = 2;

    protected Situacao getSituacaoTecnico(TecnicoDTO tecnicoDTO) {
        switch (tecnicoDTO.getSituacao()){
            case "ativo" -> { return Situacao.ATIVO;}
            case "licença" -> { return Situacao.LICENCA;}
            case "desativado" -> { return Situacao.DESATIVADO;}
        }
        throw new SituacaoNotFoundException();
    }
    protected List<Especialidade> getEspecialidadesTecnico(TecnicoDTO tecnico){
        if(tecnico.getEspecialidades_Ids().isEmpty()){
            throw new EspecialidadesTecnicoEmptyException();
        }
        List<Especialidade> especialidades = new ArrayList<>();
        for (Integer id : tecnico.getEspecialidades_Ids()) {
            Optional<Especialidade> especialidadeOptional = especialidadeRepository.findById(id);
            if (especialidadeOptional.isEmpty()){
                throw new EspecialidadeNotFoundException();
            }
            especialidades.add(especialidadeOptional.get());
        }
        return especialidades;
    }
    protected void verifyIfTecnicoExists(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new TecnicoNotFoundException();
        }
    }
    protected void verifyFieldsOfTecnico(Tecnico tecnico){
        if(StringUtils.isBlank(tecnico.getNome())) {
            throw new TecnicoNotValidException("O 'nome' do técnico não pode ser vazio!");
        }
        if(tecnico.getNome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O 'nome' do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO));
        }
        if(StringUtils.isBlank(tecnico.getSobrenome())) {
            throw new TecnicoNotValidException("O 'sobrenome' do técnico não pode ser vazio!");
        }
        if(tecnico.getSobrenome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O 'sobrenome' do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO));
        }
        if(tecnico.getTelefoneCelular().isBlank() && tecnico.getTelefoneFixo().isBlank()) {
            throw new TecnicoNotValidException("O técnico precisa ter no mínimo um telefone cadastrado, celular ou Fixo!");
        }
        if((!tecnico.getTelefoneCelular().isEmpty() && tecnico.getTelefoneCelular().length() < 11) || (!tecnico.getTelefoneFixo().isEmpty() && tecnico.getTelefoneFixo().length() < 11)) {
            throw new TecnicoNotValidException("O telefone precisa ter 11 números!");
        }
    }
}
