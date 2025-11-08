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
    private final ITechnicianRepository technicianRepository;
    private final ISpecialtyRepository specialtyRepository;
    private final Clock clock;
    private final Logger logger = LoggerFactory.getLogger(TechnicianService.class);

    public TecnicoWithSpecialityResponse fetchOneById(Integer id) {
        logger.debug("DEBUG - Fetching technician by ID: {}", id);
        Technician technician = getTecnicoById(id);
        logger.info("INFO - Technician found: id={}, nome={}", id, technician.getNome());

        return new TecnicoWithSpecialityResponse(technician);
    }

    public PageResponse<TecnicoResponse> fetchListByFilter(TecnicoRequestFilter filtroRequest, PageFilterRequest pageFilterRequest) {
        logger.debug("DEBUG - Fetching technicians with filter: {}", filtroRequest);
        PageResponse<TecnicoResponse> tecnicos = technicianRepository.filter(filtroRequest.toTechnicianFilter(), pageFilterRequest.toPageFilter())
                .map(TecnicoResponse::new);
        logger.info("INFO - Found {} technicians with filter: {}", tecnicos.getPage().getTotalElements(), filtroRequest);

        return tecnicos;
    }

    public List<TecnicoDisponibilidadeResponse> fetchListAvailability(Integer especialidadeId) {
        int intervaloDeDias = (LocalDate.now(clock).getDayOfWeek().getValue() > 4) ? 4 : 3;
        logger.debug("DEBUG - Fetching technicians availability with day interval of: {}", intervaloDeDias);

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
        logger.info("INFO - Found {} technicians available with interval={}, specialtyId={}", tecnicos.size(), intervaloDeDias, especialidadeId);

        return tecnicos;
    }

    public TecnicoWithSpecialityResponse create(TecnicoRequest tecnicoRequest) {
        logger.info("INFO - Creating new Technician");

        Technician newTechnician = technicianRepository.save(
                tecnicoRequest.toTechnician(getEspecialidadesTecnico(tecnicoRequest.especialidades_Ids()))
        );
        logger.info("INFO - Technician Created successfully with id={}", newTechnician.getId());

        return new TecnicoWithSpecialityResponse(newTechnician);
    }

    public TecnicoWithSpecialityResponse update(Integer id, TecnicoRequest tecnicoRequest) {
        logger.info("INFO - Updating technician id={}", id);
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
        logger.info("INFO - technician updated id={}", technicianUpdated.getId());

        return new TecnicoWithSpecialityResponse(technicianUpdated);
    }

    public void disableListByIds(List<Integer> ids) {
        logger.info("INFO - Disabling technicians by ids: {}", ids);

        List<Technician> tecnicos = technicianRepository.findAllById(ids);
        tecnicos.forEach(Technician::disable);

        if (!tecnicos.isEmpty())
            technicianRepository.saveAll(tecnicos);
    }

    protected Technician getTecnicoById(Integer id) {
        return technicianRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("ERROR - Technician with id={} not found", id);
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
            logger.warn("WARN - Specialties not found for ids={}", missing);
            throw new SpecialtyNotFoundException();
        }

        return specialties;
    }
}