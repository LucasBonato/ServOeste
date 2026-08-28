package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.requests.SpecialtyRequest;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyInUseException;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyNameAlreadyUsedException;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyProtectedException;
import com.serv.oeste.domain.exceptions.technician.SpecialtyNotFoundException;
import com.serv.oeste.domain.valueObjects.Specialty;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyService.class);

    private final ISpecialtyRepository specialtyRepository;

    public List<EspecialidadeResponse> findAll() {
        return specialtyRepository.findAll(false).stream()
                .map(EspecialidadeResponse::new)
                .toList();
    }

    public EspecialidadeResponse create(SpecialtyRequest request) {
        LOGGER.info(
                "specialty.create.started conhecimento={}",
                request.conhecimento()
        );

        if (specialtyRepository.findByName(request.conhecimento()).isPresent()) {
            LOGGER.error(
                    "specialty.create.already-exists conhecimento={}",
                    request.conhecimento()
            );
            throw new SpecialtyNameAlreadyUsedException();
        }

        Specialty specialty = specialtyRepository.save(Specialty.create(request.conhecimento()));

        LOGGER.info(
                "specialty.create.completed specialtyId={} conhecimento={}",
                specialty.id(),
                specialty.conhecimento()
        );

        return new EspecialidadeResponse(specialty);
    }

    public EspecialidadeResponse update(Integer id, SpecialtyRequest request) {
        LOGGER.info(
                "specialty.update.started specialtyId={} newConhecimentoName={}",
                id,
                request.conhecimento()
        );

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "specialty.update.not-found specialtyId={}",
                            id
                    );
                    return new SpecialtyNotFoundException();
                });

        if (specialty.isOutros()) {
            LOGGER.error(
                    "specialty.update.blocked-outros specialtyId={} conhecimento={} newConhecimentoName={}",
                    id,
                    specialty.conhecimento(),
                    request.conhecimento()
            );
            throw new SpecialtyProtectedException();
        }

        if (
                !specialty.conhecimento().equalsIgnoreCase(request.conhecimento()) &&
                specialtyRepository.findByName(request.conhecimento()).isPresent()
        ) {
            LOGGER.error(
                    "specialty.update.already-exists-name specialtyId={} conhecimento={} newConhecimentoName={}",
                    id,
                    specialty.conhecimento(),
                    request.conhecimento()
            );
            throw new SpecialtyNameAlreadyUsedException();
        }

        specialty.rename(request.conhecimento());

        LOGGER.info(
                "specialty.update.name-changed specialtyId={} newConhecimentoName={}",
                id,
                specialty.conhecimento()
        );

        specialty = specialtyRepository.save(specialty);

        LOGGER.info(
                "specialty.update.completed specialtyId={} newConhecimentoName={}",
                id,
                specialty.conhecimento()
        );

        return new EspecialidadeResponse(specialty);
    }

    public void deactivate(Integer id) {
        LOGGER.info(
                "specialty.deactivate.started specialtyId={}",
                id
        );

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "specialty.deactivate.not-found specialtyId={}",
                            id
                    );
                    return new SpecialtyNotFoundException();
                });

        if (specialty.isOutros()) {
            LOGGER.error(
                    "specialty.deactivate.blocked-outros specialtyId={} conhecimento={}",
                    id,
                    specialty.conhecimento()
            );
            throw new SpecialtyProtectedException();
        }

        if (specialtyRepository.countTechniciansBySpecialtyId(id) > 0) {
            LOGGER.error(
                    "specialty.deactivate.is-in-use specialtyId={} conhecimento={}",
                    id,
                    specialty.conhecimento()
            );
            throw new SpecialtyInUseException();
        }

        specialty.deactivate();
        specialtyRepository.save(specialty);
        LOGGER.info(
                "specialty.deactivate.completed specialtyId={} newConhecimentoName={} ativo={}",
                id,
                specialty.conhecimento(),
                specialty.isAtivo()
        );
    }
}