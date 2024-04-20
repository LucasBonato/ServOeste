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

    @Cacheable("allTecnicos")
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

    public ResponseEntity<List<Tecnico>> getByNomeOrSobrenome(String nomeOuSobrenome) {
        List<Tecnico> tecnicos = tecnicoRepository.findByNomeOuSobrenome(nomeOuSobrenome);
        for (Tecnico tecnico : tecnicos) {
            List<Integer> ids_Especialidades = tecnicoRepository.findByIdEspecialidade(tecnico.getId());
            tecnico.setEspecialidades(getEspecialidadesTecnico(ids_Especialidades));
        }
        return ResponseEntity.ok(tecnicos);
    }

    public ResponseEntity<List<String>> getAllConhecimentos() {
        return ResponseEntity.ok(especialidadeRepository.findAllConhecimento());
    }

    public ResponseEntity<Void> create(TecnicoDTO tecnicoDTO) {
        Tecnico tecnico = new Tecnico(tecnicoDTO);
        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));

        tecnicoRepository.save(tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Void> update(Integer id, TecnicoDTO tecnicoDTO) {
        verifyIfTecnicoExists(id);

        Tecnico tecnico = new Tecnico(tecnicoDTO);

        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoDTO));
        tecnico.setSituacao(getSituacaoTecnico(tecnicoDTO));

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

    public ResponseEntity disableAList(List<Integer> ids) {
        for (Integer id : ids) {
            disabled(id);
        }
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
    private List<Especialidade> getEspecialidadesTecnico(List<Integer> ids_Especialidades){
        List<Especialidade> especialidades = new ArrayList<>();
        for (Integer id : ids_Especialidades) {
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
        final Integer CODIGO_NOME_SOBRENOME = 1;
        final Integer CODIGO_TELEFONE_CELULAR = 2;
        final Integer CODIGO_TELEFONE_FIXO = 3;
        final Integer CODIGO_TELEFONES = 4;
        if(StringUtils.isBlank(tecnico.getNome())) {
            throw new TecnicoNotValidException("O Nome do técnico não pode ser vazio!", CODIGO_NOME_SOBRENOME);
        }
        if(tecnico.getNome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O Nome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), CODIGO_NOME_SOBRENOME);
        }
        if(StringUtils.isBlank(tecnico.getSobrenome())) {
            throw new TecnicoNotValidException("Digite Nome e Sobrenome!", CODIGO_NOME_SOBRENOME);
        }
        if(tecnico.getSobrenome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O Sobrenome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), CODIGO_NOME_SOBRENOME);
        }
        if(tecnico.getTelefoneCelular().isBlank() && tecnico.getTelefoneFixo().isBlank()) {
            throw new TecnicoNotValidException("O técnico precisa ter no mínimo um telefone cadastrado!", CODIGO_TELEFONES);
        }
        if(tecnico.getTelefoneCelular().length() < 11 && !tecnico.getTelefoneCelular().isEmpty()){
            throw new TecnicoNotValidException("Telefone celular inválido!", CODIGO_TELEFONE_CELULAR);
        }
        if(tecnico.getTelefoneFixo().length() < 11 && !tecnico.getTelefoneFixo().isEmpty()) {
            throw new TecnicoNotValidException("Telefone Fixo Inválido!", CODIGO_TELEFONE_FIXO);
        }
    }
}
