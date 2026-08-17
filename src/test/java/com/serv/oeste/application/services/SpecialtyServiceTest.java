package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.requests.SpecialtyRequest;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyInUseException;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyNameAlreadyUsedException;
import com.serv.oeste.domain.exceptions.specialty.SpecialtyProtectedException;
import com.serv.oeste.domain.exceptions.technician.SpecialtyNotFoundException;
import com.serv.oeste.domain.valueObjects.Specialty;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceTest {
    @Mock private ISpecialtyRepository specialtyRepository;
    @InjectMocks private SpecialtyService specialtyService;

    private final Specialty ADEGA = new Specialty(2, "Adega");
    private final Specialty OUTROS = new Specialty(1, "Outros");

    @Nested
    class FindAll {
        @Test
        void findAll_ShouldReturnAllSpecialtiesOrderedById() {
            when(specialtyRepository.findAll(false)).thenReturn(List.of(OUTROS, ADEGA));

            List<EspecialidadeResponse> response = specialtyService.findAll();

            assertEquals(2, response.size());
            assertEquals(1, response.getFirst().id());
            assertEquals("Outros", response.getFirst().conhecimento());
            assertTrue(response.getFirst().ativo());

            verify(specialtyRepository).findAll(false);
        }

        @Test
        void findAll_NoSpecialties_ShouldReturnEmptyList() {
            when(specialtyRepository.findAll(false)).thenReturn(List.of());

            List<EspecialidadeResponse> response = specialtyService.findAll();

            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class Create {
        @Test
        void create_ValidName_ShouldCreateAndReturnResponse() {
            SpecialtyRequest request = new SpecialtyRequest("Geladeira");
            Specialty created = Specialty.restore(7, "Geladeira", true);

            when(specialtyRepository.findByName("Geladeira")).thenReturn(Optional.empty());
            when(specialtyRepository.save(any(Specialty.class))).thenReturn(created);

            EspecialidadeResponse response = specialtyService.create(request);

            assertEquals(7, response.id());
            assertEquals("Geladeira", response.conhecimento());
            assertTrue(response.ativo());

            verify(specialtyRepository).save(any(Specialty.class));
        }

        @Test
        void create_NameAlreadyUsed_ShouldThrowSpecialtyNameAlreadyUsedException() {
            SpecialtyRequest request = new SpecialtyRequest("Adega");

            when(specialtyRepository.findByName("Adega")).thenReturn(Optional.of(ADEGA));

            assertThrows(
                    SpecialtyNameAlreadyUsedException.class,
                    () -> specialtyService.create(request)
            );

            verify(specialtyRepository, never()).save(any(Specialty.class));
        }
    }

    @Nested
    class Update {
        @Test
        void update_ValidRename_ShouldRenameAndReturnResponse() {
            SpecialtyRequest request = new SpecialtyRequest("Geladeira Frost Free");
            Specialty existing = new Specialty(7, "Geladeira");

            when(specialtyRepository.findById(7)).thenReturn(Optional.of(existing));
            when(specialtyRepository.findByName("Geladeira Frost Free")).thenReturn(Optional.empty());
            when(specialtyRepository.save(any(Specialty.class))).thenReturn(existing);

            EspecialidadeResponse response = specialtyService.update(7, request);

            assertEquals("Geladeira Frost Free", response.conhecimento());
            assertEquals(7, response.id());
            assertTrue(response.ativo());
        }

        @Test
        void update_SameName_ShouldSkipDuplicateCheckAndSucceed() {
            SpecialtyRequest request = new SpecialtyRequest("Adega");

            when(specialtyRepository.findById(2)).thenReturn(Optional.of(ADEGA));
            when(specialtyRepository.save(any(Specialty.class))).thenReturn(ADEGA);

            EspecialidadeResponse response = specialtyService.update(2, request);

            assertEquals("Adega", response.conhecimento());
            verify(specialtyRepository, never()).findByName(anyString());
        }

        @Test
        void update_NameAlreadyUsed_ShouldThrowSpecialtyNameAlreadyUsedException() {
            SpecialtyRequest request = new SpecialtyRequest("Outros");

            when(specialtyRepository.findById(2)).thenReturn(Optional.of(ADEGA));
            when(specialtyRepository.findByName("Outros")).thenReturn(Optional.of(OUTROS));

            assertThrows(
                    SpecialtyNameAlreadyUsedException.class,
                    () -> specialtyService.update(2, request)
            );

            verify(specialtyRepository, never()).save(any(Specialty.class));
        }

        @Test
        void update_Outros_ShouldThrowSpecialtyProtectedException() {
            SpecialtyRequest request = new SpecialtyRequest("Generico");

            when(specialtyRepository.findById(1)).thenReturn(Optional.of(OUTROS));

            assertThrows(
                    SpecialtyProtectedException.class,
                    () -> specialtyService.update(1, request)
            );

            verify(specialtyRepository, never()).save(any(Specialty.class));
        }

        @Test
        void update_NotFound_ShouldThrowSpecialtyNotFoundException() {
            SpecialtyRequest request = new SpecialtyRequest("Geladeira");

            when(specialtyRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(
                    SpecialtyNotFoundException.class,
                    () -> specialtyService.update(999, request)
            );
        }
    }

    @Nested
    class Deactivate {
        @Test
        void deactivate_NotInUse_ShouldDeactivateAndSave() {
            Specialty specialty = new Specialty(7, "Geladeira");

            when(specialtyRepository.findById(7)).thenReturn(Optional.of(specialty));
            when(specialtyRepository.countTechniciansBySpecialtyId(7)).thenReturn(0L);
            when(specialtyRepository.save(any(Specialty.class))).thenReturn(specialty);

            specialtyService.deactivate(7);

            assertFalse(specialty.isAtivo());
            verify(specialtyRepository).save(specialty);
        }

        @Test
        void deactivate_InUseByTechnicians_ShouldThrowSpecialtyInUseException() {
            Specialty specialty = new Specialty(7, "Geladeira");

            when(specialtyRepository.findById(7)).thenReturn(Optional.of(specialty));
            when(specialtyRepository.countTechniciansBySpecialtyId(7)).thenReturn(3L);

            assertThrows(
                    SpecialtyInUseException.class,
                    () -> specialtyService.deactivate(7)
            );

            verify(specialtyRepository, never()).save(any(Specialty.class));
        }

        @Test
        void deactivate_Outros_ShouldThrowSpecialtyProtectedException() {
            when(specialtyRepository.findById(1)).thenReturn(Optional.of(OUTROS));

            assertThrows(
                    SpecialtyProtectedException.class,
                    () -> specialtyService.deactivate(1)
            );

            verify(specialtyRepository, never()).save(any(Specialty.class));
        }

        @Test
        void deactivate_NotFound_ShouldThrowSpecialtyNotFoundException() {
            when(specialtyRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(
                    SpecialtyNotFoundException.class,
                    () -> specialtyService.deactivate(999)
            );
        }
    }
}