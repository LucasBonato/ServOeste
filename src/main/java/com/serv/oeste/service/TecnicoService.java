package com.serv.oeste.service;

import com.serv.oeste.exception.tecnico.*;
import com.serv.oeste.models.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.tecnico.TecnicoSpecifications;
import com.serv.oeste.repository.EspecialidadeRepository;
import com.serv.oeste.repository.TecnicoRepository;
import com.serv.oeste.models.dtos.reponses.TecnicoResponse;
import com.serv.oeste.models.tecnico.Especialidade;
import com.serv.oeste.models.enums.Situacao;
import com.serv.oeste.models.tecnico.Tecnico;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {
    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private EspecialidadeRepository especialidadeRepository;

    public ResponseEntity<Tecnico> getOne(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new TecnicoNotFoundException();
        }
        return ResponseEntity.ok(tecnicoOptional.get());
    }

    public ResponseEntity<List<Tecnico>> getBy(TecnicoRequestFilter filtroRequest) {
        Specification<Tecnico> specification = Specification.where(null);

        if (filtroRequest.id() != null) {
            specification = specification.and(TecnicoSpecifications.hasId(filtroRequest.id()));
        }
        if (StringUtils.isNotBlank(filtroRequest.nome())) {
            specification = specification.and(TecnicoSpecifications.hasNomeCompleto(filtroRequest.nome()));
        }
        if (StringUtils.isNotBlank(filtroRequest.situacao())) {
            specification = specification.and(TecnicoSpecifications.hasSituacao(filtroRequest.situacao()));
        }
        if (StringUtils.isNotBlank(filtroRequest.equipamento())) {
            specification = specification.and(TecnicoSpecifications.hasEquipamento(filtroRequest.equipamento()));
        }
        if (StringUtils.isNotBlank(filtroRequest.telefone())) {
            specification = specification.and(TecnicoSpecifications.hasTelefone(filtroRequest.telefone()));
        }

        List<Tecnico> response = tecnicoRepository.findAll(specification);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> create(TecnicoResponse tecnicoResponse) {
        Tecnico tecnico = new Tecnico(tecnicoResponse);
        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoResponse));

        tecnicoRepository.save(tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Void> update(Integer id, TecnicoResponse tecnicoResponse) {
        verifyIfTecnicoExists(id);

        Tecnico tecnico = new Tecnico(tecnicoResponse);

        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoResponse));
        tecnico.setSituacao(getSituacaoTecnico(tecnicoResponse));

        tecnico.setId(id);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> disableAList(List<Integer> ids) {
        for (Integer id : ids) {
            verifyIfTecnicoExists(id);

            Tecnico tecnico = tecnicoRepository.findById(id).get();
            tecnico.setSituacao(Situacao.DESATIVADO);

            tecnicoRepository.save(tecnico);
        }
        return ResponseEntity.ok().build();
    }

    private Situacao getSituacaoTecnico(TecnicoResponse tecnicoResponse) {
        switch (tecnicoResponse.getSituacao().toLowerCase()){
            case "ativo" -> { return Situacao.ATIVO;}
            case "licença" -> { return Situacao.LICENCA;}
            case "desativado" -> { return Situacao.DESATIVADO;}
        }
        throw new SituacaoNotFoundException();
    }
    private List<Especialidade> getEspecialidadesTecnico(TecnicoResponse tecnico){
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
        final int MINIMO_DE_CARACTERES_ACEITO = 2;
        if(StringUtils.isBlank(tecnico.getNome())) {
            throw new TecnicoNotValidException("O Nome do técnico não pode ser vazio!", Codigo.NOMESOBRENOME);
        }
        if(tecnico.getNome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O Nome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if(StringUtils.isBlank(tecnico.getSobrenome())) {
            throw new TecnicoNotValidException("Digite Nome e Sobrenome!", Codigo.NOMESOBRENOME);
        }
        if(tecnico.getSobrenome().length() < MINIMO_DE_CARACTERES_ACEITO){
            throw new TecnicoNotValidException(String.format("O Sobrenome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if(tecnico.getTelefoneCelular().isBlank() && tecnico.getTelefoneFixo().isBlank()) {
            throw new TecnicoNotValidException("O técnico precisa ter no mínimo um telefone cadastrado!", Codigo.TELEFONES);
        }
        if(tecnico.getTelefoneCelular().length() < 11 && !tecnico.getTelefoneCelular().isEmpty()){
            throw new TecnicoNotValidException("Telefone celular inválido!", Codigo.TELEFONECELULAR);
        }
        if(tecnico.getTelefoneFixo().length() < 10 && !tecnico.getTelefoneFixo().isEmpty()) {
            throw new TecnicoNotValidException("Telefone Fixo Inválido!", Codigo.TELEFONEFIXO);
        }
    }
}
