package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.application.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.application.exceptions.technician.TechnicianNotValidException;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Codigo;
import com.serv.oeste.domain.enums.Situacao;
import com.serv.oeste.domain.valueObjects.TechnicianFilter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {
    @Mock private ITechnicianRepository technicianRepository;
    @Mock private ISpecialtyRepository specialtyRepository;
    @InjectMocks private TechnicianService technicianService;

    public static final Specialty ADEGA = new Specialty(1, "Adega");
    public static final Specialty BEBEDOURO = new Specialty(2, "Bebedouro");
    public static final Specialty CLIMATIZADOR = new Specialty(3, "Climatizador");
    public static final Specialty COOLER = new Specialty(4, "Cooler");
    public static final Specialty FRIGOBAR = new Specialty(5, "Frigobar");
    public static final Specialty GELADEIRA = new Specialty(6, "Geladeira");
    public static final Specialty LAVA_LOUCA = new Specialty(7, "Lava Louça");
    public static final Specialty LAVA_ROUPA = new Specialty(8, "Lava Roupa");
    public static final Specialty MICROONDAS = new Specialty(9, "Microondas");
    public static final Specialty PURIFICADOR = new Specialty(10, "Purificador");
    public static final Specialty SECADORA = new Specialty(11, "Secadora");
    public static final Specialty OUTROS = new Specialty(12, "Outros");

    // MethodName_StateUnderTest_ExpectedBehavior

    @Nested
    class FetchOneById {
        @Test
        void fetchOneById_TechnicianExists_ShouldReturnTechnicianWithSuccess() {
            // Arrange
            int technicianId = 1;

            List<Specialty> specialties = List.of(
                    BEBEDOURO,
                    FRIGOBAR
            );

            Technician technician = new Technician(
                    technicianId,
                    "João",
                    "Silveira Raposo",
                    "1187642508",
                    "11974016758",
                    Situacao.ATIVO,
                    specialties
            );

            when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));

            // Act
            ResponseEntity<TecnicoWithSpecialityResponse> response = technicianService.fetchOneById(technicianId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());

            TecnicoWithSpecialityResponse body = response.getBody();
            assertNotNull(body);
            assertEquals("João", body.nome());
            assertEquals("Silveira Raposo", body.sobrenome());
            assertEquals("1187642508", body.telefoneFixo());
            assertEquals("11974016758", body.telefoneCelular());
            assertEquals(Situacao.ATIVO, body.situacao());

            assertNotNull(body.especialidades());
            assertEquals(specialties.size(), body.especialidades().size());

            assertThat(body.especialidades())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            new EspecialidadeResponse(BEBEDOURO),
                            new EspecialidadeResponse(FRIGOBAR)
                    );
        }

        @Test
        void fetchOneById_TechnicianDoesNotExists_ShouldThrowTechnicianNotFoundException() {
            // Arrange
            int idToBeFound = 1;
            when(technicianRepository.findById(idToBeFound)).thenReturn(Optional.empty());

            // Act
            TechnicianNotFoundException exception = assertThrows(
                    TechnicianNotFoundException.class,
                    () -> technicianService.fetchOneById(idToBeFound)
            );

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Técnico não encontrado!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TECNICO.getI(), exception.getExceptionResponse().getIdError());
        }
    }

    @Nested
    class FetchListByFilter {
        final Technician JOAO = new Technician(1, "João", "Silva", "11332211", "11999887766", Situacao.ATIVO, List.of());
        final Technician MARIA = new Technician(2, "Maria", "Souza", "11223344", "11912345678", Situacao.DESATIVADO, List.of());

        @Test
        void fetchListByFilter_NoFilters_ShouldReturnAllTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, null, null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();

            when(technicianRepository.filter(filter)).thenReturn(List.of(JOAO, MARIA));

            // Act
            ResponseEntity<List<TecnicoResponse>> response = technicianService.fetchListByFilter(filterRequest);

            // Assert
            List<TecnicoResponse> body = response.getBody();
            assertNotNull(body);
            assertEquals(2, body.size());
            assertTrue(body.stream().anyMatch(t -> t.id().equals(JOAO.getId())));
            assertTrue(body.stream().anyMatch(t -> t.id().equals(MARIA.getId())));
        }

        @Test
        void fetchListByFilter_WithNomeFilter_ShouldReturnMatchingTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, "João", null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();

            when(technicianRepository.filter(filter)).thenReturn(List.of(JOAO));

            // Act
            ResponseEntity<List<TecnicoResponse>> response = technicianService.fetchListByFilter(filterRequest);

            // Assert
            List<TecnicoResponse> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals("João", body.getFirst().nome());
        }

        @Test
        void fetchListByFilter_WithSituacaoFilter_ShouldReturnOnlyMatchingTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, null, "ATIVO", null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();

            when(technicianRepository.filter(filter)).thenReturn(List.of(JOAO));

            // Act
            ResponseEntity<List<TecnicoResponse>> response = technicianService.fetchListByFilter(filterRequest);

            // Assert
            List<TecnicoResponse> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(Situacao.ATIVO, body.getFirst().situacao());
        }

        @Test
        void fetchListByFilter_WithInvalidFilters_ShouldReturnEmptyList() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, "Fulano", null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();

            when(technicianRepository.filter(filter)).thenReturn(List.of());

            // Act
            ResponseEntity<List<TecnicoResponse>> response = technicianService.fetchListByFilter(filterRequest);

            // Assert
            List<TecnicoResponse> body = response.getBody();
            assertNotNull(body);
            assertTrue(body.isEmpty());
        }
    }

    @Nested
    class FetchListAvailability {
        @Test
        void fetchListAvailability() {
        }
    }

    @Nested
    class Create {
        @Test
        void create_ValidRequest_ShouldCreateTechnicianSuccessfully() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            Technician technician = new Technician(
                    1,
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Situacao.ATIVO,
                    List.of(ADEGA, PURIFICADOR)
            );

            when(specialtyRepository.findById(ADEGA.getId())).thenReturn(Optional.of(ADEGA));
            when(specialtyRepository.findById(PURIFICADOR.getId())).thenReturn(Optional.of(PURIFICADOR));
            when(technicianRepository.save(any(Technician.class))).thenReturn(technician);

            // Act
            ResponseEntity<TecnicoWithSpecialityResponse> response = technicianService.create(request);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            TecnicoWithSpecialityResponse body = response.getBody();
            assertNotNull(body);

            assertEquals(1, body.id());
            assertEquals("Railson", body.nome());
            assertEquals("Ferreira dos Santos", body.sobrenome());
            assertEquals("", body.telefoneFixo());
            assertEquals("11968949278", body.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), body.situacao().getSituacao());

            assertThat(body.especialidades())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            new EspecialidadeResponse(ADEGA),
                            new EspecialidadeResponse(PURIFICADOR)
                    );

            verify(technicianRepository).save(any(Technician.class));
        }

        @Test
        void create_ValidRequestWithAnotherSituation_ShouldCreateTechnicianSuccessfullyIgnoringPassedSituation() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "DESATIVADO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            Technician technician = new Technician(
                    1,
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Situacao.ATIVO,
                    List.of(ADEGA, PURIFICADOR)
            );

            when(specialtyRepository.findById(ADEGA.getId())).thenReturn(Optional.of(ADEGA));
            when(specialtyRepository.findById(PURIFICADOR.getId())).thenReturn(Optional.of(PURIFICADOR));
            when(technicianRepository.save(any(Technician.class))).thenReturn(technician);

            // Act
            ResponseEntity<TecnicoWithSpecialityResponse> response = technicianService.create(request);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            TecnicoWithSpecialityResponse body = response.getBody();
            assertNotNull(body);

            assertEquals(1, body.id());
            assertEquals("Railson", body.nome());
            assertEquals("Ferreira dos Santos", body.sobrenome());
            assertEquals("", body.telefoneFixo());
            assertEquals("11968949278", body.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), body.situacao().getSituacao());

            assertThat(body.especialidades())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            new EspecialidadeResponse(ADEGA),
                            new EspecialidadeResponse(PURIFICADOR)
                    );

            verify(technicianRepository).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithBlankName_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Nome do técnico não pode ser vazio!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithShortName_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "R",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Nome do técnico precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithBlankSurname_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Digite Nome e Sobrenome!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithShortSurname_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "F",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Sobrenome do técnico precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithNoPhone_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O técnico precisa ter no mínimo um telefone cadastrado!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONES.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestLengthCellPhone_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Telefone celular inválido!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONECELULAR.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestLengthLandLine_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "11876351",
                    "",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.getId(), PURIFICADOR.getId())
            );

            // Act
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Telefone Fixo Inválido!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONEFIXO.getI(), exception.getExceptionResponse().getIdError());
            verify(technicianRepository, never()).save(any(Technician.class));
        }
    }

    @Nested
    class Update {
        @Test
        void update_ValidRequestWithExistingTechnician_ShouldUpdateTechnicianAndReturnResponse() {
            // Arrange
            int technicianToBeUpdatedId = 1;

            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "",
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(MICROONDAS.getId(), OUTROS.getId())
            );

            Technician technician = new Technician(
                    technicianToBeUpdatedId,
                    "NomeAntigo",
                    "Sobrenome Antigo",
                    "1187652345",
                    "11968949278",
                    Situacao.DESATIVADO,
                    new ArrayList<>()
            );

            when(specialtyRepository.findById(MICROONDAS.getId())).thenReturn(Optional.of(MICROONDAS));
            when(specialtyRepository.findById(OUTROS.getId())).thenReturn(Optional.of(OUTROS));
            when(technicianRepository.findById(technicianToBeUpdatedId)).thenReturn(Optional.of(technician));
            when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
            // Act
            ResponseEntity<TecnicoWithSpecialityResponse> response = technicianService.update(technicianToBeUpdatedId, request);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            TecnicoWithSpecialityResponse body = response.getBody();
            assertNotNull(body);
            assertEquals(technicianToBeUpdatedId, body.id());
            assertEquals("Railson", body.nome());
            assertEquals("Ferreira dos Santos", body.sobrenome());
            assertEquals("", body.telefoneFixo());
            assertEquals("11968949278", body.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), body.situacao().getSituacao());

            assertThat(body.especialidades())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            new EspecialidadeResponse(MICROONDAS),
                            new EspecialidadeResponse(OUTROS)
                    );

            verify(technicianRepository).findById(technicianToBeUpdatedId);
            verify(technicianRepository).save(any(Technician.class));
        }
    }

    @Nested
    class DisableListByIds {
        @Test
        void disableListByIds() {
        }
    }
}