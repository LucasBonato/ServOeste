package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.domain.exceptions.client.ClientNotFoundException;
import com.serv.oeste.domain.exceptions.service.ServiceNotFoundException;
import com.serv.oeste.domain.exceptions.service.ServiceNotValidException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;
import com.serv.oeste.factories.ClientFactory;
import com.serv.oeste.factories.ServiceFactory;
import com.serv.oeste.factories.TechnicianFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    @Mock private IServiceRepository serviceRepository;
    @Mock private ClientService clientService;
    @Mock private TechnicianService technicianService;
    @InjectMocks private ServiceService serviceService;

    @Nested
    class FetchListByFilter {
        @Test
        void fetchListByFilter_ValidFilterWithOneResult_ReturnsSingleServicoResponse() {
            // Arrange
            ServicoRequestFilter filterRequest = mock(ServicoRequestFilter.class);
            ServiceFilter filter = mock(ServiceFilter.class);
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(filterRequest.toServiceFilter()).thenReturn(filter);

            LocalDate hoje = LocalDate.now();
            LocalDate inicioGarantia = hoje.minusDays(2);
            LocalDate fimGarantia = hoje.plusDays(5);

            Service service = ServiceFactory.createWithGarantia(inicioGarantia, fimGarantia);

            when(serviceRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(service),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ServicoResponse> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ServicoResponse> content = response.getContent();
            assertNotNull(response);
            assertEquals(1, content.size());
            assertEquals("Monitor", content.getFirst().equipamento());
            assertEquals("João Silva", content.getFirst().nomeCliente());
            assertEquals("Carlos Silva", content.getFirst().nomeTecnico());
        }

        @Test
        void fetchListByFilter_ValidFilterWithNoResults_ReturnsEmptyList() {
            // Arrange
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            ServicoRequestFilter filterRequest = mock(ServicoRequestFilter.class);
            ServiceFilter filter = mock(ServiceFilter.class);
            when(filterRequest.toServiceFilter()).thenReturn(filter);
            when(serviceRepository.filter(any(), any())).thenReturn(new PageResponse<>(
                    Collections.emptyList(),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ServicoResponse> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            assertTrue(response.getContent().isEmpty());
        }

        @Test
        void fetchListByFilter_ServiceWithoutTechnician_ReturnsResponseWithNullTechnicianFields() {
            // Arrange
            ServicoRequestFilter filterRequest = mock(ServicoRequestFilter.class);
            ServiceFilter filter = mock(ServiceFilter.class);
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            when(filterRequest.toServiceFilter()).thenReturn(filter);

            Service service = ServiceFactory.create(
                    2,
                    "Impressora",
                    "HP",
                    "Campinas",
                    "Erro no cartucho",
                    SituacaoServico.AGUARDANDO_ATENDIMENTO,
                    HorarioPrevisto.TARDE,
                    160.00,
                    FormaPagamento.PIX,
                    100.00,
                    60.00,
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    ClientFactory.createDefault(),
                    null
            );

            when(serviceRepository.filter(any(), any())).thenReturn(new PageResponse<>(
                    List.of(service),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ServicoResponse> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            ServicoResponse servicoResponse = response.getContent().getFirst();
            assertEquals("Impressora", servicoResponse.equipamento());
            assertEquals("João Silva", servicoResponse.nomeCliente());
            assertNull(servicoResponse.nomeTecnico());
            assertNull(servicoResponse.idTecnico());
        }

        @Test
        void fetchListByFilter_ServiceWithWarrantyInDateRange_ReturnsGarantiaTrue() {
            // Arrange
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            ServicoRequestFilter filterRequest = mock(ServicoRequestFilter.class);
            ServiceFilter filter = mock(ServiceFilter.class);
            when(filterRequest.toServiceFilter()).thenReturn(filter);

            LocalDate hoje = LocalDate.now();
            LocalDate inicioGarantia = hoje.minusDays(2);
            LocalDate fimGarantia = hoje.plusDays(5);

            Service service = ServiceFactory.createWithGarantia(inicioGarantia, fimGarantia);
            when(serviceRepository.filter(any(), any())).thenReturn(new PageResponse<>(
                    List.of(service),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ServicoResponse> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            assertTrue(response.getContent().getFirst().garantia());
        }
    }

    @Nested
    class CadastrarComClienteExistente {

        @Test
        void cadastrarComClienteExistente_ValidRequest_ShouldReturnCreatedResponseWithService() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(1, 1);
            Client cliente = mock(Client.class);
            Technician tecnico = mock(Technician.class);

            when(clientService.getClienteById(1)).thenReturn(cliente);
            when(serviceRepository.save(any())).thenReturn(ServiceFactory.createDefault());
            when(technicianService.getTecnicoById(1)).thenReturn(tecnico);

            // Act
           ServicoResponse response = serviceService.create(validRequest, 1);

            // Assert
            assertNotNull(response);
        }

        @Test
        void cadastrarComClienteExistente_NullClientId_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithNullClientId = ServiceFactory.createServiceRequestWithNullClientId();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(requestWithNullClientId, null)
            );
        }

        @Test
        void cadastrarComClienteExistente_MissingRequiredFields_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidRequest = ServiceFactory.createServiceRequestWithInvalidHorario();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(invalidRequest, invalidRequest.idCliente())
            );
        }

        @Test
        void cadastrarComClienteExistente_InvalidDescriptionLength_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithShortDescription = ServiceFactory.createServiceRequestWithShortDescription();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(requestWithShortDescription, requestWithShortDescription.idCliente())
            );
        }

        @Test
        void cadastrarComClienteExistente_InvalidDescriptionWordCount_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithFewWords = ServiceFactory.createServiceRequestWithFewWords();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(requestWithFewWords, requestWithFewWords.idCliente())
            );

        }

        @Test
        void cadastrarComClienteExistente_ClientDoesNotExist_ShouldThrowClientNotFoundException() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(999, 1);

            when(clientService.getClienteById(999)).thenThrow(new ClientNotFoundException());

            // Act & Assert
            assertThrows(
                    ClientNotFoundException.class,
                    () -> serviceService.create(validRequest, 999)
            );
        }

        @Test
        void cadastrarComClienteExistente_TechnicianDoesNotExist_ShouldThrowTechnicianNotFoundException() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(1, 999);

            when(clientService.getClienteById(1)).thenReturn(mock(Client.class));
            when(technicianService.getTecnicoById(999)).thenThrow(new TechnicianNotFoundException());

            // Act & Assert
            assertThrows(
                    TechnicianNotFoundException.class,
                    () -> serviceService.create(validRequest, 1)
            );
        }
    }

    @Nested
    class CadastrarComClienteNaoExistente {
        private final ClienteRequest VALID_CLIENT_REQUEST = ClientFactory.createValidClienteRequest();
        private final ServicoRequest VALID_SERVICE_REQUEST = ServiceFactory.createValidServiceRequest(null, 1);
        private final ClienteResponse CLIENT_RESPONSE = ClientFactory.createValidClienteResponse();
        private final ServicoResponse SERVICE_RESPONSE = ServiceFactory.createValidServicoResponse();

        @Test
        void cadastrarComClienteNaoExistente_ValidRequest_ShouldReturnCreatedResponse() {
            // Arrange
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act & Assert
            serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id());
        }

        @Test
        void cadastrarComClienteNaoExistente_ValidClientAndService_ShouldReturnCreatedWithServiceResponse() {
            // Arrange
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act
            ServicoResponse response = serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id());

            // Assert
            assertNotNull(response);
            assertEquals(SERVICE_RESPONSE, response);
            verify(clientService).getClienteById(CLIENT_RESPONSE.id());
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceReturnsNull_ShouldThrowServiceNotValidException() {
            // Arrange & Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id())
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_InvalidServiceRequest_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidRequest = ServiceFactory.createServiceRequestMissingRequiredFields();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(invalidRequest, invalidRequest.idCliente())
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceThrowsException_ShouldPropagateException() {
            // Arrange & Act & Assert
            assertThrows(
                    RuntimeException.class,
                    () -> serviceService.create(VALID_SERVICE_REQUEST, null)
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ServiceCreationFails_ShouldNotReturnCreated() {
            // Arrange & Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id())
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceReturnsNullId_ShouldThrowServiceNotValidException() {
            // Arrange
            ClienteResponse responseWithNullId = new ClienteResponse(null, "Null ID Client", null, null, null, null, null);

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(VALID_SERVICE_REQUEST, responseWithNullId.id())
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_VerifyClientServiceInteraction_ShouldCallClientServiceExactlyOnce() {
            // Arrange
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act
            serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id());

            // Assert
            verify(clientService, times(1)).getClienteById(CLIENT_RESPONSE.id());
            verifyNoMoreInteractions(clientService);
        }

        @Test
        void cadastrarComClienteNaoExistente_InvalidServiceDescription_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidDescRequest = ServiceFactory.createServiceRequestWithShortDescription();

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(invalidDescRequest, invalidDescRequest.idCliente())
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidServiceRequests")
        void cadastrarComClienteNaoExistente_InvalidServiceRequests_ShouldThrowServiceNotValidException(ServicoRequest invalidRequest) {
            // Arrange
            lenient().when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(CLIENT_RESPONSE);

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(invalidRequest, invalidRequest.idCliente())
            );
        }

        private static Stream<Arguments> provideInvalidServiceRequests() {
            return Stream.of(
                    Arguments.of(ServiceFactory.createServiceRequestMissingRequiredFields()),
                    Arguments.of(ServiceFactory.createServiceRequestWithShortDescription()),
                    Arguments.of(ServiceFactory.createServiceRequestWithFewWords()),
                    Arguments.of(ServiceFactory.createServiceRequestWithInvalidHorario())
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientCreatedButServiceCreationFails_ShouldNotReturnCreated() {
            // Arrange
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(mock(Client.class));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.create(VALID_SERVICE_REQUEST, CLIENT_RESPONSE.id())
            );

            // Verify client was still created despite service failure
            verify(clientService).getClienteById(CLIENT_RESPONSE.id());
        }
    }

    @Nested
    class Update {
        private final Integer EXISTING_SERVICE_ID = 1;
        private final Integer NON_EXISTENT_SERVICE_ID = 999;
        private final ServicoUpdateRequest VALID_UPDATE_REQUEST = ServiceFactory.createValidServicoUpdateRequest();
        private final Service EXISTING_SERVICE = ServiceFactory.createValidServiceWithId(EXISTING_SERVICE_ID);
        private final Client VALID_CLIENT = ClientFactory.createDefault();
        private final Technician VALID_TECHNICIAN = TechnicianFactory.createDefault();

        @Test
        void update_ValidRequestForExistingService_ShouldReturnOkWithUpdatedService() {
            // Arrange
            Service updatedService = ServiceFactory.createUpdatedService(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST, VALID_CLIENT, VALID_TECHNICIAN);
            ServicoResponse expectedResponse = ServiceFactory.createServicoResponse(updatedService);

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));
            when(clientService.getClienteById(VALID_UPDATE_REQUEST.idCliente())).thenReturn(VALID_CLIENT);
            when(technicianService.getTecnicoById(VALID_UPDATE_REQUEST.idTecnico())).thenReturn(VALID_TECHNICIAN);
            when(serviceRepository.save(any(Service.class))).thenReturn(updatedService);

            // Act
            ServicoResponse response = serviceService.update(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST);

            // Assert
            assertEquals(expectedResponse, response);
        }

        @Test
        void update_NonExistentService_ShouldThrowServiceNotFoundException() {
            // Arrange
            when(serviceRepository.findById(NON_EXISTENT_SERVICE_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                    ServiceNotFoundException.class,
                    () -> serviceService.update(NON_EXISTENT_SERVICE_ID, VALID_UPDATE_REQUEST)
            );
        }

        @Test
        void update_NullClientId_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoUpdateRequest requestWithNullClient = ServiceFactory.createServicoUpdateRequestWithNullClient();

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, requestWithNullClient)
            );
        }

        @Test
        void update_NullTechnicianForNonPendingStatus_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoUpdateRequest requestWithNullTechnician = ServiceFactory.createServicoUpdateRequestWithNullTechnician();

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, requestWithNullTechnician)
            );
        }

        @Test
        void update_ValidRequest_ShouldVerifyRepositorySaveCalled() {
            // Arrange
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));
            when(clientService.getClienteById(VALID_UPDATE_REQUEST.idCliente())).thenReturn(VALID_CLIENT);
            when(technicianService.getTecnicoById(VALID_UPDATE_REQUEST.idTecnico())).thenReturn(VALID_TECHNICIAN);
            when(serviceRepository.save(any(Service.class))).thenReturn(EXISTING_SERVICE);

            // Act
            serviceService.update(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST);

            // Assert
            verify(serviceRepository).save(any(Service.class));
        }

        @Test
        void update_ClientNotFound_ShouldThrowClientNotFoundException() {
            // Arrange
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));
            when(clientService.getClienteById(VALID_UPDATE_REQUEST.idCliente()))
                    .thenThrow(new ClientNotFoundException());

            // Act & Assert
            assertThrows(
                    ClientNotFoundException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST)
            );
        }

        @Test
        void update_TechnicianNotFound_ShouldThrowTechnicianNotFoundException() {
            // Arrange
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));
            when(clientService.getClienteById(VALID_UPDATE_REQUEST.idCliente())).thenReturn(VALID_CLIENT);
            when(technicianService.getTecnicoById(VALID_UPDATE_REQUEST.idTecnico()))
                    .thenThrow(new TechnicianNotFoundException());

            // Act & Assert
            assertThrows(
                    TechnicianNotFoundException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidUpdateScenarios")
        void update_InvalidScenarios_ShouldThrowServiceNotValidException(ServicoUpdateRequest invalidRequest) {
            // Arrange
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            assertThrows(
                    RuntimeException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, invalidRequest)
            );
        }

        private static Stream<Arguments> provideInvalidUpdateScenarios() {
            return Stream.of(
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativeValue()),
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativeCommission()),
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativePartsValue())
            );
        }

        @Test
        void update_ValidRequestForPendingStatus_ShouldNotRequireTechnician() {
            // Arrange
            ServicoUpdateRequest pendingRequest = ServiceFactory.createUpdateRequestForPendingStatus();
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));
            when(clientService.getClienteById(pendingRequest.idCliente())).thenReturn(VALID_CLIENT);
            when(serviceRepository.save(any(Service.class))).thenReturn(EXISTING_SERVICE);

            // Act & Assert
            assertDoesNotThrow(() -> serviceService.update(EXISTING_SERVICE_ID, pendingRequest));
        }
    }

    @Nested
    class DeleteListByIds {
        @Test
        void deleteListByIds_IdsValidos_RetornaOkEDeletaServicos() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);

            // Act
            serviceService.deleteListByIds(ids);

            // Assert
            verify(serviceRepository).deleteAllById(ids);
        }
    }
}