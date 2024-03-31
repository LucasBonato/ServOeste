package com.serv.oeste.Service;

import com.serv.oeste.Exception.Tecnico.*;
import com.serv.oeste.Repository.EspecialidadeRepository;
import com.serv.oeste.Repository.TecnicoRepository;
import com.serv.oeste.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Models.Especialidade;
import com.serv.oeste.Models.Situacao;
import com.serv.oeste.Models.Tecnico;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {
    private final int MINIMO_DE_CARACTERES_ACEITO = 2;

    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private EspecialidadeRepository especialidadeRepository;

    public ResponseEntity<List<Tecnico>> getAllBySituacao(Situacao situacao) {
        List<Tecnico> tecnicos = switch (situacao){
            case ATIVO -> tecnicoRepository.findAllBySituacao(Situacao.ATIVO);
            case LICENCA -> tecnicoRepository.findAllBySituacao(Situacao.LICENCA);
            case DESATIVADO -> tecnicoRepository.findAllBySituacao(Situacao.DESATIVADO);
        };
        return ResponseEntity.ok(tecnicos);
    }

    @Cacheable("tecnicoCache")
    public ResponseEntity<Tecnico> getOne(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new TecnicoNotFoundException();
        }
        return ResponseEntity.ok(tecnicoOptional.get());
    }

    public ResponseEntity<Void> create(TecnicoDTO tecnicoDTO) {
        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));

        verifyFieldsOfTecnico(tecnico);

        tecnicoRepository.save(tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Void> update(Integer id, TecnicoDTO tecnicoDTO) {
        verifyIfTecnicoExists(id);

        Tecnico tecnico = new Tecnico(tecnicoDTO);
        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));
        tecnico.setSituacao(getSituacaoTecnico(tecnicoDTO));

        verifyFieldsOfTecnico(tecnico);

        tecnico.setId(id);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> disabled(Integer id) {
        verifyIfTecnicoExists(id);

        Tecnico tecnico = tecnicoRepository.findById(id).get();
        tecnico.setSituacao(Situacao.DESATIVADO);

        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    private Situacao getSituacaoTecnico(TecnicoDTO tecnicoDTO) {
        switch (tecnicoDTO.getSituacao()){
            case "ativo" -> { return Situacao.ATIVO;}
            case "licença" -> { return Situacao.LICENCA;}
            case "desativado" -> { return Situacao.DESATIVADO;}
        }
        throw new SituacaoNotFoundException();
    }
    private List<Especialidade> getEspecialidadesTecnico(TecnicoDTO tecnico){
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
    private void verifyIfTecnicoExists(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new TecnicoNotFoundException();
        }
    }
    private void verifyFieldsOfTecnico(Tecnico tecnico){
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
