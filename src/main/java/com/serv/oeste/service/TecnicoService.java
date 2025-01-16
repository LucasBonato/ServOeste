package com.serv.oeste.service;

import com.serv.oeste.exception.tecnico.*;
import com.serv.oeste.models.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.models.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.specifications.SpecificationBuilder;
import com.serv.oeste.models.tecnico.*;
import com.serv.oeste.repository.EspecialidadeRepository;
import com.serv.oeste.repository.TecnicoRepository;
import com.serv.oeste.models.dtos.reponses.TecnicoRequest;
import com.serv.oeste.models.enums.Situacao;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TecnicoService {
    private final TecnicoRepository tecnicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public ResponseEntity<Tecnico> getOne(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if (tecnicoOptional.isEmpty()) {
            throw new TecnicoNotFoundException();
        }
        return ResponseEntity.ok(tecnicoOptional.get());
    }

    public ResponseEntity<List<Tecnico>> getBy(TecnicoRequestFilter filtroRequest) {
        Specification<Tecnico> specification = new SpecificationBuilder<Tecnico>()
                .addIfNotNull(filtroRequest.id(), TecnicoSpecifications::hasId)
                .addIf(StringUtils::isNotBlank, filtroRequest.nome(), TecnicoSpecifications::hasNomeCompleto)
                .addIf(StringUtils::isNotBlank, filtroRequest.situacao(), TecnicoSpecifications::hasSituacao)
                .addIf(StringUtils::isNotBlank, filtroRequest.equipamento(), TecnicoSpecifications::hasEquipamento)
                .addIf(StringUtils::isNotBlank, filtroRequest.telefone(), TecnicoSpecifications::hasTelefone)
                .build();

//        if (filtroRequest.id() != null) {
//            specification = specification.and(TecnicoSpecifications.hasId(filtroRequest.id()));
//        }
//        if (StringUtils.isNotBlank(filtroRequest.nome())) {
//            specification = specification.and(TecnicoSpecifications.hasNomeCompleto(filtroRequest.nome()));
//        }
//        if (StringUtils.isNotBlank(filtroRequest.situacao())) {
//            specification = specification.and(TecnicoSpecifications.hasSituacao(filtroRequest.situacao()));
//        }
//        if (StringUtils.isNotBlank(filtroRequest.equipamento())) {
//            specification = specification.and(TecnicoSpecifications.hasEquipamento(filtroRequest.equipamento()));
//        }
//        if (StringUtils.isNotBlank(filtroRequest.telefone())) {
//            specification = specification.and(TecnicoSpecifications.hasTelefone(filtroRequest.telefone()));
//        }

        List<Tecnico> response = tecnicoRepository.findAll(specification);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> getDadosDisponibilidade(Integer especialidadeId) {
        int intervaloDeDias = (LocalDate.now().getDayOfWeek().getValue() > 4) ? 4 : 3;

        List<TecnicoDisponibilidade> tecnicosRaw = tecnicoRepository.getDisponibilidadeTecnicosPeloConhecimento(intervaloDeDias, especialidadeId)
                .stream()
                .map(TecnicoDisponibilidade::new)
                .toList();

        List<TecnicoDisponibilidadeResponse> tecnicos = tecnicosRaw.stream()
                .collect(Collectors.groupingBy(TecnicoDisponibilidade::getId))
                .entrySet().stream()
                .map(tecnico -> {
                    List<TecnicoDisponibilidade> rawList = tecnico.getValue();
                    String nome = rawList.getFirst().getNome();
                    Integer id = tecnico.getKey();
                    Integer quantidadeTotal = rawList.stream()
                            .mapToInt(TecnicoDisponibilidade::getQuantidade)
                            .sum();
                    List<Disponibilidade> disponibilidades = rawList.stream()
                            .map(this::getDisponibilidadeFromRaw)
                            .collect(Collectors.toList());
                    return new TecnicoDisponibilidadeResponse(id, nome, quantidadeTotal, disponibilidades);
                })
                .toList();

/*        Map<Integer, TecnicoDisponibilidadeResponse> tecnicoMap = tecnicosRaw.stream()
                .collect(
                        Collectors.groupingBy(
                                TecnicoDisponibilidade::getId,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        rawList -> {
                                            String nome = rawList.getFirst().getNome();
                                            Integer id = rawList.getFirst().getId();
                                            Integer quantidadeTotal = rawList.stream()
                                                    .mapToInt(TecnicoDisponibilidade::getQuantidade)
                                                    .sum();
                                            List<Disponibilidade> disponibilidades = rawList.stream()
                                                    .map(this::getDisponibilidadeFromRaw)
                                                    .collect(Collectors.toList());
                                            return new TecnicoDisponibilidadeResponse(id, nome, quantidadeTotal, disponibilidades);
                                        }
                                )
                        )
                );

        List<TecnicoDisponibilidadeResponse> tecnicos = new ArrayList<>(tecnicoMap.values());*/

        return ResponseEntity.ok(tecnicos);
    }

    public ResponseEntity<Tecnico> create(TecnicoRequest tecnicoRequest) {
        Tecnico tecnico = new Tecnico(tecnicoRequest);
        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoRequest));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tecnicoRepository.save(tecnico));
    }

    public ResponseEntity<Void> update(Integer id, TecnicoRequest tecnicoRequest) {
        Tecnico tecnico = getTecnicoById(id);

        tecnico.setAll(tecnicoRequest);

        verifyFieldsOfTecnico(tecnico);

        tecnico.setEspecialidades(getEspecialidadesTecnico(tecnicoRequest));
        tecnico.setSituacao(getSituacaoTecnico(tecnicoRequest));
        tecnico.setId(id);
        tecnicoRepository.save(tecnico);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> disableAList(List<Integer> ids) {
        List<Tecnico> tecnicos = ids.stream()
                .map(this::getTecnicoById)
                .peek(tecnico -> tecnico.setSituacao(Situacao.DESATIVADO))
                .collect(Collectors.toList());

        tecnicoRepository.saveAll(tecnicos);

        return ResponseEntity.ok().build();
    }

    protected Tecnico getTecnicoById(Integer id) {
        return tecnicoRepository.findById(id).orElseThrow(TecnicoNotFoundException::new);
    }

    private Disponibilidade getDisponibilidadeFromRaw(TecnicoDisponibilidade tecnicoDisponibilidade) {
        return new Disponibilidade(
            tecnicoDisponibilidade.getData(),
            tecnicoDisponibilidade.getDia(),
            getDayNameOfTheWeek(tecnicoDisponibilidade.getDia()),
            tecnicoDisponibilidade.getPeriodo(),
            tecnicoDisponibilidade.getQuantidade()
        );
    }

    private String getDayNameOfTheWeek(Integer day) {
        return switch (day) {
            case 1 -> "Domingo";
            case 2 -> "Segunda";
            case 3 -> "Terça";
            case 4 -> "Quarta";
            case 5 -> "Quinta";
            case 6 -> "Sexta";
            case 7 -> "Sábado";
            default -> "Dia da semana não encontrado!";
        };
    }

    private Situacao getSituacaoTecnico(TecnicoRequest tecnicoResponse) {
        switch (tecnicoResponse.getSituacao().toLowerCase()) {
            case "ativo" -> {
                return Situacao.ATIVO;
            }
            case "licença" -> {
                return Situacao.LICENCA;
            }
            case "desativado" -> {
                return Situacao.DESATIVADO;
            }
        }
        throw new SituacaoNotFoundException();
    }

    private List<Especialidade> getEspecialidadesTecnico(TecnicoRequest tecnico) {
        if (tecnico.getEspecialidades_Ids().isEmpty()) {
            throw new EspecialidadesTecnicoEmptyException();
        }

        return tecnico.getEspecialidades_Ids().stream()
                .map(id -> especialidadeRepository.findById(id).orElseThrow(EspecialidadeNotFoundException::new))
                .collect(Collectors.toList());
    }

    private void verifyFieldsOfTecnico(Tecnico tecnico) {
        final int MINIMO_DE_CARACTERES_ACEITO = 2;
        if (StringUtils.isBlank(tecnico.getNome())) {
            throw new TecnicoNotValidException("O Nome do técnico não pode ser vazio!", Codigo.NOMESOBRENOME);
        }
        if (tecnico.getNome().length() < MINIMO_DE_CARACTERES_ACEITO) {
            throw new TecnicoNotValidException(String.format("O Nome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if (StringUtils.isBlank(tecnico.getSobrenome())) {
            throw new TecnicoNotValidException("Digite Nome e Sobrenome!", Codigo.NOMESOBRENOME);
        }
        if (tecnico.getSobrenome().length() < MINIMO_DE_CARACTERES_ACEITO) {
            throw new TecnicoNotValidException(String.format("O Sobrenome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if (tecnico.getTelefoneCelular().isBlank() && tecnico.getTelefoneFixo().isBlank()) {
            throw new TecnicoNotValidException("O técnico precisa ter no mínimo um telefone cadastrado!", Codigo.TELEFONES);
        }
        if (tecnico.getTelefoneCelular().length() < 11 && !tecnico.getTelefoneCelular().isEmpty()) {
            throw new TecnicoNotValidException("Telefone celular inválido!", Codigo.TELEFONECELULAR);
        }
        if (tecnico.getTelefoneFixo().length() < 10 && !tecnico.getTelefoneFixo().isEmpty()) {
            throw new TecnicoNotValidException("Telefone Fixo Inválido!", Codigo.TELEFONEFIXO);
        }
    }
}