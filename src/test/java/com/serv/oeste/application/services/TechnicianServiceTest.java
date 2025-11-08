package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.domain.contracts.repositories.ISpecialtyRepository;
import com.serv.oeste.domain.contracts.repositories.ITechnicianRepository;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotValidException;
import com.serv.oeste.domain.exceptions.valueObjects.PhoneNotValidException;
import com.serv.oeste.domain.valueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

            Technician technician = Technician.restore(
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
            TecnicoWithSpecialityResponse response = technicianService.fetchOneById(technicianId);

            // Assert
            assertNotNull(response);
            assertEquals("João", response.nome());
            assertEquals("Silveira Raposo", response.sobrenome());
            assertEquals("1187642508", response.telefoneFixo());
            assertEquals("11974016758", response.telefoneCelular());
            assertEquals(Situacao.ATIVO, response.situacao());

            assertNotNull(response.especialidades());
            assertEquals(specialties.size(), response.especialidades().size());

            assertThat(response.especialidades())
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

            // Act & Assert
            assertThrows(
                    TechnicianNotFoundException.class,
                    () -> technicianService.fetchOneById(idToBeFound)
            );
        }
    }

    @Nested
    class FetchListByFilter {
        final Technician JOAO = Technician.restore(1, "João", "Silva", "1133221145", "11999887766", Situacao.ATIVO, List.of());
        final Technician MARIA = Technician.restore(2, "Maria", "Souza", "1122334417", "11912345678", Situacao.DESATIVADO, List.of());

        @Test
        void fetchListByFilter_NoFilters_ShouldReturnAllTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, null, null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(technicianRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(JOAO, MARIA),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<TecnicoResponse> response = technicianService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<TecnicoResponse> body = response.getContent();
            assertEquals(2, body.size());
            assertTrue(body.stream().anyMatch(t -> t.id().equals(JOAO.getId())));
            assertTrue(body.stream().anyMatch(t -> t.id().equals(MARIA.getId())));
        }

        @Test
        void fetchListByFilter_WithNomeFilter_ShouldReturnMatchingTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, "João", null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(technicianRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(JOAO),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<TecnicoResponse> response = technicianService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<TecnicoResponse> body = response.getContent();
            assertEquals(1, body.size());
            assertEquals("João", body.getFirst().nome());
        }

        @Test
        void fetchListByFilter_WithSituacaoFilter_ShouldReturnOnlyMatchingTechnicians() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, null, "ATIVO", null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(technicianRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(JOAO),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<TecnicoResponse> response = technicianService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<TecnicoResponse> body = response.getContent();
            assertEquals(1, body.size());
            assertEquals(Situacao.ATIVO, body.getFirst().situacao());
        }

        @Test
        void fetchListByFilter_WithInvalidFilters_ShouldReturnEmptyList() {
            // Arrange
            TecnicoRequestFilter filterRequest = new TecnicoRequestFilter(null, "Fulano", null, null, null);
            TechnicianFilter filter = filterRequest.toTechnicianFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(technicianRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<TecnicoResponse> response = technicianService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<TecnicoResponse> body = response.getContent();
            assertTrue(body.isEmpty());
        }
    }

    @Nested
    class FetchListAvailability {
        @BeforeEach
        void setup() {
            LocalDate now = LocalDate.now();
            Clock clock = Clock.fixed(now.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

            technicianService = new TechnicianService(
                    technicianRepository,
                    specialtyRepository,
                    clock
            );
        }

        @Test
        void fetchListAvailability_WhenTechniciansExistForSpecialty_ShouldReturnListWithGroupedAvailability() {
            // Arrange
            int specialtyId = 1;
            int todayDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
            int expectedInterval = todayDayOfWeek > 4 ? 4 : 3;

            List<TechnicianAvailability> rawList = List.of(
                    new TechnicianAvailability(1, "João", LocalDate.now(), 2, "MANHÃ", 2),
                    new TechnicianAvailability(1, "João",LocalDate.now(), 2, "TARDE", 3),
                    new TechnicianAvailability(2, "Maria", LocalDate.now(), 2, "TARDE", 1)
            );

            when(technicianRepository.getTechnicianAvailabilityBySpecialty(expectedInterval, specialtyId))
                    .thenReturn(rawList);

            // Act
            List<TecnicoDisponibilidadeResponse> response = technicianService.fetchListAvailability(specialtyId);

            // Assert
            assertNotNull(response);
            assertEquals(2, response.size());

            TecnicoDisponibilidadeResponse joao = response.stream()
                    .filter(t -> t.getNome().equals("João"))
                    .findFirst()
                    .orElseThrow();
            assertEquals(5, joao.getQuantidadeTotalServicos());

            TecnicoDisponibilidadeResponse maria = response.stream()
                    .filter(t -> t.getNome().equals("Maria"))
                    .findFirst()
                    .orElseThrow();
            assertEquals(1, maria.getQuantidadeTotalServicos());

            // Verifica dias e períodos
            assertThat(joao.getDisponibilidades()).hasSize(2);
        }

        @Test
        void fetchListAvailability_WhenNoTechnicianMatchesSpecialty_ShouldReturnEmptyList() {
            // Arrange
            when(technicianRepository.getTechnicianAvailabilityBySpecialty(anyInt(), anyInt()))
                    .thenReturn(Collections.emptyList());

            // Act
            List<TecnicoDisponibilidadeResponse> response = technicianService.fetchListAvailability(999);

            // Assert
            assertNotNull(response);
            assertTrue(response.isEmpty());
        }

        @Test
        void fetchListAvailability_shouldUseCorrectDayInterval_WhenTodayIsFridayOrLater() {
            // Arrange
            LocalDate friday = LocalDate.of(2024, 5, 31); // sexta-feira
            Clock fixedClock = Clock.fixed(friday.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

            TechnicianService service = new TechnicianService(
                    technicianRepository,
                    specialtyRepository,
                    fixedClock
            );

            when(technicianRepository.getTechnicianAvailabilityBySpecialty(eq(4), anyInt()))
                    .thenReturn(Collections.emptyList());

            // Act
            service.fetchListAvailability(1);

            // Assert
            verify(technicianRepository).getTechnicianAvailabilityBySpecialty(4, 1);
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
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            Technician technician = Technician.restore(
                    1,
                    "Railson",
                    "Ferreira dos Santos",
                    null,
                    "11968949278",
                    Situacao.ATIVO,
                    List.of(ADEGA, PURIFICADOR)
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));
            when(technicianRepository.save(any(Technician.class))).thenReturn(technician);

            // Act
            TecnicoWithSpecialityResponse response = technicianService.create(request);

            // Assert
            assertNotNull(response);

            assertEquals(1, response.id());
            assertEquals("Railson", response.nome());
            assertEquals("Ferreira dos Santos", response.sobrenome());
            assertNull(response.telefoneFixo());
            assertEquals("11968949278", response.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), response.situacao().getSituacao());

            assertThat(response.especialidades())
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
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "DESATIVADO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            Technician technician = Technician.restore(
                    1,
                    "Railson",
                    "Ferreira dos Santos",
                    null,
                    "11968949278",
                    Situacao.ATIVO,
                    List.of(ADEGA, PURIFICADOR)
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));
            when(technicianRepository.save(any(Technician.class))).thenReturn(technician);

            // Act
            TecnicoWithSpecialityResponse response = technicianService.create(request);

            // Assert

            assertNotNull(response);

            assertEquals(1, response.id());
            assertEquals("Railson", response.nome());
            assertEquals("Ferreira dos Santos", response.sobrenome());
            assertNull(response.telefoneFixo());
            assertEquals("11968949278", response.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), response.situacao().getSituacao());

            assertThat(response.especialidades())
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
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(LAVA_LOUCA.id(), LAVA_ROUPA.id())
            );

            when(specialtyRepository.findAllById(List.of(LAVA_LOUCA.id(), LAVA_ROUPA.id()))).thenReturn(List.of(LAVA_LOUCA, LAVA_ROUPA));

            // Act
            assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithShortName_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "R",
                    "Ferreira dos Santos",
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithBlankSurname_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    null,
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithShortSurname_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "F",
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestWithNoPhone_ShouldThrowTechnicianNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    null,
                    null,
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    TechnicianNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestLengthCellPhone_ShouldThrowPhoneNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    null,
                    "11968949",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    PhoneNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
            verify(technicianRepository, never()).save(any(Technician.class));
        }

        @Test
        void create_InvalidRequestLengthLandLine_ShouldThrowPhoneNotValidException() {
            // Arrange
            TecnicoRequest request = new TecnicoRequest(
                    "Railson",
                    "Ferreira dos Santos",
                    "11876351",
                    null,
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(ADEGA.id(), PURIFICADOR.id())
            );

            when(specialtyRepository.findAllById(List.of(ADEGA.id(), PURIFICADOR.id()))).thenReturn(List.of(ADEGA, PURIFICADOR));

            // Act
            assertThrows(
                    PhoneNotValidException.class,
                    () -> technicianService.create(request)
            );

            // Assert
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
                    null,
                    "11968949278",
                    Enum.valueOf(Situacao.class, "ATIVO"),
                    List.of(MICROONDAS.id(), OUTROS.id())
            );

            Technician technician = Technician.restore(
                    technicianToBeUpdatedId,
                    "NomeAntigo",
                    "Sobrenome Antigo",
                    "1187652345",
                    "11968949278",
                    Situacao.DESATIVADO,
                    new ArrayList<>()
            );

            when(specialtyRepository.findAllById(List.of(MICROONDAS.id(), OUTROS.id()))).thenReturn(List.of(MICROONDAS, OUTROS));
            when(technicianRepository.findById(technicianToBeUpdatedId)).thenReturn(Optional.of(technician));
            when(technicianRepository.findById(technicianToBeUpdatedId)).thenReturn(Optional.of(technician));
            when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
            // Act
            TecnicoWithSpecialityResponse response = technicianService.update(technicianToBeUpdatedId, request);

            // Assert
            assertNotNull(response);
            assertEquals(technicianToBeUpdatedId, response.id());
            assertEquals("Railson", response.nome());
            assertEquals("Ferreira dos Santos", response.sobrenome());
            assertNull(response.telefoneFixo());
            assertEquals("11968949278", response.telefoneCelular());
            assertEquals(Situacao.ATIVO.getSituacao(), response.situacao().getSituacao());

            assertThat(response.especialidades())
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
        void disableListByIds_DiableTechnicians_ShouldDisableAllTechnicians() {
            // Arrange
            Technician technician1 = Technician.restore(
                    1,
                    "Railson",
                    "Ferreira dos Santos",
                    null,
                    "11968949278",
                    Situacao.ATIVO,
                    List.of(GELADEIRA, COOLER)
            );

            Technician technician2 = Technician.restore(
                    2,
                    "Tinoco",
                    "Vordez Silva",
                    "1198762345",
                    null,
                    Situacao.ATIVO,
                    List.of(SECADORA, CLIMATIZADOR)
            );

            List<Integer> ids = List.of(1, 2);

            when(technicianRepository.findAllById(ids)).thenReturn(List.of(technician1, technician2));
            // Act
            technicianService.disableListByIds(ids);

            // Assert

            assertEquals(Situacao.DESATIVADO, technician1.getSituacao());
            assertEquals(Situacao.DESATIVADO, technician2.getSituacao());

            verify(technicianRepository, times(1)).saveAll(any());
            verify(technicianRepository).saveAll(
                    argThat(list ->
                        list.size() == 2 &&
                        list.contains(technician1) &&
                        list.contains(technician2) &&
                        list.stream().allMatch(technician -> technician.getSituacao() == Situacao.DESATIVADO)
                    )
            );
        }

        @Test
        void disableListByIds_WithEmptyList_ShouldReturnOkAndNotCallRepository() {
            // Act
            technicianService.disableListByIds(List.of());

            // Assert
            verify(technicianRepository, never()).saveAll(any());
        }
    }
}