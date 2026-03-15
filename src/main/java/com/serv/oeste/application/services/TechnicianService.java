package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.domain.entities.technician.Availability;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.valueObjects.TechnicianAvailability;
import com.serv.oeste.domain.exceptions.technician.SpecialtyNotFoundException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.domain.valueObjects.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicianService.class);

    private final ITechnicianRepository technicianRepository;
    private final ISpecialtyRepository specialtyRepository;
    private final Clock clock;

    public TecnicoWithSpecialityResponse fetchOneById(Integer id) {
        LOGGER.debug("technician.fetch-by-id.started id={}", id);
        Technician technician = getTecnicoById(id);
        LOGGER.info("technician.fetch-by-id.succeeded id={} nome={}", id, technician.getNome());

        return new TecnicoWithSpecialityResponse(technician);
    }

    public PageResponse<TecnicoResponse> fetchListByFilter(TecnicoRequestFilter filtroRequest, PageFilterRequest pageFilterRequest) {
        LOGGER.debug("technician.fetch-list.started filter={}", filtroRequest);
        PageResponse<TecnicoResponse> tecnicos = technicianRepository
                .filter(filtroRequest.toTechnicianFilter(), pageFilterRequest.toPageFilter())
                .map(TecnicoResponse::new);

        LOGGER.debug(
                "technician.fetch-list.completed totalElements={} totalPages={} filter={}",
                tecnicos.getPage().totalElements(),
                tecnicos.getPage().totalPages(),
                filtroRequest
        );

        return tecnicos;
    }

    public List<TecnicoDisponibilidadeResponse> fetchListAvailability(Integer especialidadeId) {
        int intervaloDeDias = (LocalDate.now(clock).getDayOfWeek().getValue() > 4) ? 4 : 3;
        LOGGER.debug("technician.fetch-availability.started interval={} specialtyId={}", intervaloDeDias, especialidadeId);

        List<TechnicianAvailability> tecnicosRaw = technicianRepository.getTechnicianAvailabilityBySpecialty(intervaloDeDias, especialidadeId);

        List<TecnicoDisponibilidadeResponse> tecnicos = tecnicosRaw.stream()
                .collect(Collectors.groupingBy(TechnicianAvailability::id))
                .entrySet().stream()
                .map(tecnico -> {
                    List<TechnicianAvailability> rawList = tecnico.getValue();
                    return new TecnicoDisponibilidadeResponse(
                            tecnico.getKey(),
                            rawList.getFirst().nome(),
                            rawList.stream().mapToInt(TechnicianAvailability::quantidade).sum(),
                            rawList.stream().map(this::getAvailabilityFromRaw).toList()
                    );
                })
                .toList();
        LOGGER.info("technician.fetch-availability.completed count={} interval={} specialtyId={}",
                tecnicos.size(),
                intervaloDeDias,
                especialidadeId
        );

        return tecnicos;
    }

    public TecnicoWithSpecialityResponse create(TecnicoRequest tecnicoRequest) {
        LOGGER.info("technician.create.started nome={}", tecnicoRequest.nome());

        Technician newTechnician = technicianRepository.save(
                tecnicoRequest.toTechnician(getEspecialidadesTecnico(tecnicoRequest.especialidades_Ids()))
        );
        LOGGER.info("technician.create.completed id={} nome={}", newTechnician.getId(), newTechnician.getNome());

        return new TecnicoWithSpecialityResponse(newTechnician);
    }

    public TecnicoWithSpecialityResponse update(Integer id, TecnicoRequest tecnicoRequest) {
        LOGGER.info("technician.update.started id={}", id);
        Technician tecnico = getTecnicoById(id);

        tecnico.update(
                tecnicoRequest.nome(),
                tecnicoRequest.sobrenome(),
                tecnicoRequest.telefoneFixo(),
                tecnicoRequest.telefoneCelular(),
                tecnicoRequest.situacao(),
                getEspecialidadesTecnico(tecnicoRequest.especialidades_Ids())
        );

        Technician technicianUpdated = technicianRepository.save(tecnico);
        LOGGER.info("technician.update.completed id={} nome={}", technicianUpdated.getId(), technicianUpdated.getNome());

        return new TecnicoWithSpecialityResponse(technicianUpdated);
    }

    public void disableListByIds(List<Integer> ids) {
        LOGGER.info("technician.disable-list.started ids={}", ids);

        List<Technician> tecnicos = technicianRepository.findAllById(ids);
        tecnicos.forEach(Technician::disable);

        if (!tecnicos.isEmpty()) {
            technicianRepository.saveAll(tecnicos);
            LOGGER.info("technician.disable-list.completed count={}", tecnicos.size());
        } else {
            LOGGER.warn("technician.disable-list.no-technicians-found ids={}", ids);
        }
    }

    protected Technician getTecnicoById(Integer id) {
        return technicianRepository
                .findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("technician.not-found id={}", id);
                    return new TechnicianNotFoundException();
                });
    }

    private Availability getAvailabilityFromRaw(TechnicianAvailability technicianAvailability) {
        return new Availability(
                technicianAvailability.data(),
                technicianAvailability.dia(),
                getDayNameOfTheWeek(DayOfWeek.of(technicianAvailability.dia())),
                technicianAvailability.periodo(),
                technicianAvailability.quantidade()
        );
    }

    private String getDayNameOfTheWeek(DayOfWeek day) {
        return switch (day) {
            case SUNDAY -> "Domingo";
            case MONDAY -> "Segunda";
            case TUESDAY -> "Terça";
            case WEDNESDAY -> "Quarta";
            case THURSDAY -> "Quinta";
            case FRIDAY -> "Sexta";
            case SATURDAY -> "Sábado";
        };
    }

    private List<Specialty> getEspecialidadesTecnico(List<Integer> specialtyIds) {
        if (specialtyIds == null || specialtyIds.isEmpty())
            return List.of();

        List<Specialty> specialties = specialtyRepository.findAllById(specialtyIds);
        Set<Integer> foundIds = specialties.stream()
                .map(Specialty::id)
                .collect(Collectors.toSet());

        List<Integer> missing = specialtyIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missing.isEmpty()) {
            LOGGER.warn("technician.specialties-not-found ids={}", missing);
            throw new SpecialtyNotFoundException();
        }

        return specialties;
    }
}