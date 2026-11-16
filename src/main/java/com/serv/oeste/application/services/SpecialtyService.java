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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private final ISpecialtyRepository specialtyRepository;

    public List<EspecialidadeResponse> findAll() {
        return specialtyRepository.findAll(false).stream()
                .map(EspecialidadeResponse::new)
                .toList();
    }

    public EspecialidadeResponse create(SpecialtyRequest request) {
        log.info(
                "specialty.create.started conhecimento={}",
                request.conhecimento()
        );

        if (specialtyRepository.findByName(request.conhecimento()).isPresent()) {
            log.error(
                    "specialty.create.already-exists conhecimento={}",
                    request.conhecimento()
            );
            throw new SpecialtyNameAlreadyUsedException();
        }

        Specialty specialty = specialtyRepository.save(Specialty.create(request.conhecimento()));

        log.info(
                "specialty.create.completed specialtyId={} conhecimento={}",
                specialty.id(),
                specialty.conhecimento()
        );

        return new EspecialidadeResponse(specialty);
    }

    public EspecialidadeResponse update(Integer id, SpecialtyRequest request) {
        log.info(
                "specialty.update.started specialtyId={} newConhecimentoName={}",
                id,
                request.conhecimento()
        );

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(
                            "specialty.update.not-found specialtyId={}",
                            id
                    );
                    return new SpecialtyNotFoundException();
                });

        if (specialty.isOutros()) {
            log.error(
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
            log.error(
                    "specialty.update.already-exists-name specialtyId={} conhecimento={} newConhecimentoName={}",
                    id,
                    specialty.conhecimento(),
                    request.conhecimento()
            );
            throw new SpecialtyNameAlreadyUsedException();
        }

        specialty.rename(request.conhecimento());

        log.info(
                "specialty.update.name-changed specialtyId={} newConhecimentoName={}",
                id,
                specialty.conhecimento()
        );

        specialty = specialtyRepository.save(specialty);

        log.info(
                "specialty.update.completed specialtyId={} newConhecimentoName={}",
                id,
                specialty.conhecimento()
        );

        return new EspecialidadeResponse(specialty);
    }

    public void deactivate(Integer id) {
        log.info(
                "specialty.deactivate.started specialtyId={}",
                id
        );

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(
                            "specialty.deactivate.not-found specialtyId={}",
                            id
                    );
                    return new SpecialtyNotFoundException();
                });

        if (specialty.isOutros()) {
            log.error(
                    "specialty.deactivate.blocked-outros specialtyId={} conhecimento={}",
                    id,
                    specialty.conhecimento()
            );
            throw new SpecialtyProtectedException();
        }

        if (specialtyRepository.countTechniciansBySpecialtyId(id) > 0) {
            log.error(
                    "specialty.deactivate.is-in-use specialtyId={} conhecimento={}",
                    id,
                    specialty.conhecimento()
            );
            throw new SpecialtyInUseException();
        }

        specialty.deactivate();
        specialtyRepository.save(specialty);
        log.info(
                "specialty.deactivate.completed specialtyId={} newConhecimentoName={} ativo={}",
                id,
                specialty.conhecimento(),
                specialty.isAtivo()
        );
    }
}