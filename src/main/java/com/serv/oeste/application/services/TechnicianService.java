package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.application.exceptions.technician.SpecialtyNotFoundException;
import com.serv.oeste.application.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.application.exceptions.technician.TechnicianNotValidException;
import com.serv.oeste.application.exceptions.technician.TechnicianSpecialtyEmptyException;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.domain.entities.technician.Availability;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.entities.technician.TechnicianAvailability;
import com.serv.oeste.domain.enums.Codigo;
import com.serv.oeste.domain.enums.Situacao;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private final ITechnicianRepository technicianRepository;
    private final ISpecialtyRepository specialtyRepository;
    private final Clock clock;

    public ResponseEntity<TecnicoWithSpecialityResponse> fetchOneById(Integer id) {
        return ResponseEntity.ok(new TecnicoWithSpecialityResponse(getTecnicoById(id)));
    }

    public ResponseEntity<List<TecnicoResponse>> fetchListByFilter(TecnicoRequestFilter filtroRequest) {
        List<TecnicoResponse> tecnicos = technicianRepository.filter(filtroRequest.toTechnicianFilter())
                .stream()
                .map(TecnicoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tecnicos);
    }

    public ResponseEntity<List<TecnicoDisponibilidadeResponse>> fetchListAvailability(Integer especialidadeId) {
        int intervaloDeDias = (LocalDate.now(clock).getDayOfWeek().getValue() > 4) ? 4 : 3;

        List<TechnicianAvailability> tecnicosRaw = technicianRepository.getTechnicianAvailabilityBySpecialty(intervaloDeDias, especialidadeId);

        List<TecnicoDisponibilidadeResponse> tecnicos = tecnicosRaw.stream()
                .collect(Collectors.groupingBy(TechnicianAvailability::getId))
                .entrySet().stream()
                .map(tecnico -> {
                    List<TechnicianAvailability> rawList = tecnico.getValue();
                    return new TecnicoDisponibilidadeResponse(
                            tecnico.getKey(),
                            rawList.getFirst().getNome(),
                            rawList.stream().mapToInt(TechnicianAvailability::getQuantidade).sum(),
                            rawList.stream().map(this::getAvailabilityFromRaw).toList()
                    );
                })
                .toList();

        return ResponseEntity.ok(tecnicos);
    }

    public ResponseEntity<TecnicoWithSpecialityResponse> create(TecnicoRequest tecnicoRequest) {
        verifyFieldsOfTecnico(tecnicoRequest);

        Technician technician = technicianRepository.save(tecnicoRequest.toTechnician());

        technician.setEspecialidades(getEspecialidadesTecnico(tecnicoRequest.especialidades_Ids()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TecnicoWithSpecialityResponse(technician));
    }

    public ResponseEntity<TecnicoWithSpecialityResponse> update(Integer id, TecnicoRequest tecnicoRequest) {
        Technician tecnico = getTecnicoById(id);

        verifyFieldsOfTecnico(tecnicoRequest);

        tecnico.setAll(
                id,
                tecnicoRequest.nome(),
                tecnicoRequest.sobrenome(),
                tecnicoRequest.telefoneFixo(),
                tecnicoRequest.telefoneCelular(),
                tecnicoRequest.situacao(),
                getEspecialidadesTecnico(tecnicoRequest.especialidades_Ids())
        );

        return ResponseEntity.ok(new TecnicoWithSpecialityResponse(technicianRepository.save(tecnico)));
    }

    public ResponseEntity<Void> disableListByIds(List<Integer> ids) {
        List<Technician> tecnicos = ids.stream()
                .map(this::getTecnicoById)
                .peek(tecnico -> tecnico.setSituacao(Situacao.DESATIVADO))
                .collect(Collectors.toList());

        if (!tecnicos.isEmpty())
            technicianRepository.saveAll(tecnicos);

        return ResponseEntity.ok().build();
    }

    protected Technician getTecnicoById(Integer id) {
        return technicianRepository.findById(id).orElseThrow(TechnicianNotFoundException::new);
    }

    private Availability getAvailabilityFromRaw(TechnicianAvailability technicianAvailability) {
        return new Availability(
                technicianAvailability.getData(),
                technicianAvailability.getDia(),
                getDayNameOfTheWeek(technicianAvailability.getDia()),
                technicianAvailability.getPeriodo(),
                technicianAvailability.getQuantidade()
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

    private List<Specialty> getEspecialidadesTecnico(List<Integer> specialtyIds) {
        if (specialtyIds.isEmpty()) {
            throw new TechnicianSpecialtyEmptyException();
        }

        return specialtyIds.stream()
                .map(id -> specialtyRepository.findById(id).orElseThrow(SpecialtyNotFoundException::new))
                .collect(Collectors.toList());
    }

    private void verifyFieldsOfTecnico(TecnicoRequest tecnicoRequest) {
        final int MINIMO_DE_CARACTERES_ACEITO = 2;
        if (StringUtils.isBlank(tecnicoRequest.nome())) {
            throw new TechnicianNotValidException("O Nome do técnico não pode ser vazio!", Codigo.NOMESOBRENOME);
        }
        if (tecnicoRequest.nome().length() < MINIMO_DE_CARACTERES_ACEITO) {
            throw new TechnicianNotValidException(String.format("O Nome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if (StringUtils.isBlank(tecnicoRequest.sobrenome())) {
            throw new TechnicianNotValidException("Digite Nome e Sobrenome!", Codigo.NOMESOBRENOME);
        }
        if (tecnicoRequest.sobrenome().length() < MINIMO_DE_CARACTERES_ACEITO) {
            throw new TechnicianNotValidException(String.format("O Sobrenome do técnico precisa ter no mínimo %d caracteres!", MINIMO_DE_CARACTERES_ACEITO), Codigo.NOMESOBRENOME);
        }
        if (tecnicoRequest.telefoneCelular().isBlank() && tecnicoRequest.telefoneFixo().isBlank()) {
            throw new TechnicianNotValidException("O técnico precisa ter no mínimo um telefone cadastrado!", Codigo.TELEFONES);
        }
        if (tecnicoRequest.telefoneCelular().length() < 11 && !tecnicoRequest.telefoneCelular().isEmpty()) {
            throw new TechnicianNotValidException("Telefone celular inválido!", Codigo.TELEFONECELULAR);
        }
        if (tecnicoRequest.telefoneFixo().length() < 10 && !tecnicoRequest.telefoneFixo().isEmpty()) {
            throw new TechnicianNotValidException("Telefone Fixo Inválido!", Codigo.TELEFONEFIXO);
        }
    }
}