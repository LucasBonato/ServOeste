package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.application.exceptions.client.ClientNotFoundException;
import com.serv.oeste.application.exceptions.service.ServiceNotFoundException;
import com.serv.oeste.application.exceptions.service.ServiceNotValidException;
import com.serv.oeste.application.exceptions.technician.TechnicianNotFoundException;
import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Codigo;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.application.factory.ClientFactory;
import com.serv.oeste.application.factory.ServiceFactory;
import com.serv.oeste.application.factory.TechnicianFactory;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.domain.valueObjects.ServiceFilter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    @Mock private IServiceRepository serviceRepository;
    @Mock private IClientRepository clientRepository;
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
            Date inicioGarantia = java.sql.Date.valueOf(hoje.minusDays(2));
            Date fimGarantia = java.sql.Date.valueOf(hoje.plusDays(5));

            Service service = ServiceFactory.createWithGarantia(inicioGarantia, fimGarantia);

            when(serviceRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(service),
                    1,
                    0,
                    10
            ));

            // Act
            ResponseEntity<PageResponse<ServicoResponse>> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response.getBody());
            List<ServicoResponse> content = response.getBody().getContent();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
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
            ResponseEntity<PageResponse<ServicoResponse>> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getContent().isEmpty());
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
                    "Tarde",
                    160.00,
                    FormaPagamento.PIX,
                    100.00,
                    60.00,
                    new Date(),
                    new Date(),
                    new Date(),
                    new Date(),
                    new Date(),
                    new Date(),
                    new Date(),
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
            ResponseEntity<PageResponse<ServicoResponse>> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            ServicoResponse servicoResponse = response.getBody().getContent().getFirst();
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
            Date inicioGarantia = java.sql.Date.valueOf(hoje.minusDays(2));
            Date fimGarantia = java.sql.Date.valueOf(hoje.plusDays(5));

            var service = ServiceFactory.createWithGarantia(inicioGarantia, fimGarantia);
            when(serviceRepository.filter(any(), any())).thenReturn(new PageResponse<>(
                    List.of(service),
                    1,
                    0,
                    10
            ));

            // Act
            ResponseEntity<PageResponse<ServicoResponse>> response = serviceService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getContent().getFirst().garantia());
        }
    }

    @Nested
    class CadastrarComClienteExistente {

        @Test
        void cadastrarComClienteExistente_ValidRequest_ShouldReturnCreatedResponseWithService() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(1, 1);

            when(clientService.getClienteById(1)).thenReturn(new Client());
            when(serviceRepository.save(any())).thenReturn(ServiceFactory.createDefault());
            when(technicianService.getTecnicoById(1)).thenReturn(new Technician());

            // Act
            ResponseEntity<ServicoResponse> response = serviceService.cadastrarComClienteExistente(validRequest);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void cadastrarComClienteExistente_NullClientId_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithNullClientId = ServiceFactory.createServiceRequestWithNullClientId();

            // Act
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteExistente(requestWithNullClientId)
            );

            // Assert
            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
            assertEquals("Não foi possível encontrar o último cliente cadastrado!", exception.getExceptionResponse().getMessage());
        }

        @Test
        void cadastrarComClienteExistente_MissingRequiredFields_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidRequest = ServiceFactory.createServiceRequestWithInvalidHorario();

            // Act
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteExistente(invalidRequest)
            );

            // Assert
            assertNotNull(exception.getExceptionResponse().getIdError());
            assertNotNull(exception.getExceptionResponse().getMessage());
        }

        @Test
        void cadastrarComClienteExistente_InvalidDescriptionLength_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithShortDescription = ServiceFactory.createServiceRequestWithShortDescription();

            // Act & Assert
            ServiceNotValidException exception = assertThrows(ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteExistente(requestWithShortDescription));

            assertEquals(Codigo.DESCRICAO.getI(), exception.getExceptionResponse().getIdError());
            assertEquals("Descrição precisa ter pelo menos 10 caracteres", exception.getExceptionResponse().getMessage());
        }

        @Test
        void cadastrarComClienteExistente_InvalidDescriptionWordCount_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest requestWithFewWords = ServiceFactory.createServiceRequestWithFewWords();

            // Act
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteExistente(requestWithFewWords)
            );

            // Assert
            assertEquals(Codigo.DESCRICAO.getI(), exception.getExceptionResponse().getIdError());
            assertEquals("Descrição precisa ter pelo menos 3 palavras", exception.getExceptionResponse().getMessage());
        }

        @Test
        void cadastrarComClienteExistente_ClientDoesNotExist_ShouldThrowClientNotFoundException() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(999, 1);

            when(clientService.getClienteById(999)).thenThrow(new ClientNotFoundException());

            // Act & Assert
            assertThrows(
                    ClientNotFoundException.class,
                    () -> serviceService.cadastrarComClienteExistente(validRequest)
            );
        }

        @Test
        void cadastrarComClienteExistente_TechnicianDoesNotExist_ShouldThrowTechnicianNotFoundException() {
            // Arrange
            ServicoRequest validRequest = ServiceFactory.createValidServiceRequest(1, 999);

            when(clientService.getClienteById(1)).thenReturn(new Client());
            when(technicianService.getTecnicoById(999)).thenThrow(new TechnicianNotFoundException());

            // Act & Assert
            assertThrows(
                    TechnicianNotFoundException.class,
                    () -> serviceService.cadastrarComClienteExistente(validRequest)
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
            when(clientService.create(any(ClienteRequest.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(CLIENT_RESPONSE));
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act
            ResponseEntity<ServicoResponse> response = serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void cadastrarComClienteNaoExistente_ValidClientAndService_ShouldReturnCreatedWithServiceResponse() {
            // Arrange
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(CLIENT_RESPONSE));
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act
            ResponseEntity<ServicoResponse> response = serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(SERVICE_RESPONSE, response.getBody());
            verify(clientService).create(VALID_CLIENT_REQUEST);
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceReturnsNull_ShouldThrowServiceNotValidException() {
            // Arrange
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(null));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST)
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_InvalidServiceRequest_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidRequest = ServiceFactory.createServiceRequestMissingRequiredFields();

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, invalidRequest)
            );

            assertNotNull(exception.getExceptionResponse().getIdError());
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceThrowsException_ShouldPropagateException() {
            // Arrange
            String errorMessage = "Erro na criação de cliente";
            when(clientService.create(VALID_CLIENT_REQUEST)).thenThrow(new RuntimeException(errorMessage));

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST)
            );

            assertEquals(errorMessage, exception.getMessage());
        }

        @Test
        void cadastrarComClienteNaoExistente_ServiceCreationFails_ShouldNotReturnCreated() {
            // Arrange
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(CLIENT_RESPONSE));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST)
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientServiceReturnsNullId_ShouldThrowServiceNotValidException() {
            // Arrange
            ClienteResponse responseWithNullId = new ClienteResponse(null, "Null ID Client", null, null, null, null, null);
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(responseWithNullId));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST)
            );

            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
            assertEquals("Não foi possível encontrar o último cliente cadastrado!", exception.getExceptionResponse().getMessage());
        }

        @Test
        void cadastrarComClienteNaoExistente_VerifyClientServiceInteraction_ShouldCallClientServiceExactlyOnce() {
            // Arrange
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(CLIENT_RESPONSE));
            when(clientService.getClienteById(CLIENT_RESPONSE.id())).thenReturn(ClientFactory.createDefault());
            when(technicianService.getTecnicoById(VALID_SERVICE_REQUEST.idTecnico())).thenReturn(TechnicianFactory.createDefault());
            when(serviceRepository.save(any(Service.class))).thenReturn(ServiceFactory.createDefault());

            // Act
            serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST);

            // Assert
            verify(clientService, times(1)).create(VALID_CLIENT_REQUEST);
            verifyNoMoreInteractions(clientService);
        }

        @Test
        void cadastrarComClienteNaoExistente_InvalidServiceDescription_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoRequest invalidDescRequest = ServiceFactory.createServiceRequestWithShortDescription();

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, invalidDescRequest)
            );

            assertEquals(Codigo.DESCRICAO.getI(), exception.getExceptionResponse().getIdError());
        }

        @ParameterizedTest
        @MethodSource("provideInvalidServiceRequests")
        void cadastrarComClienteNaoExistente_InvalidServiceRequests_ShouldThrowServiceNotValidException(ServicoRequest invalidRequest, Codigo expectedErrorCode) {
            // Arrange
            lenient().when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(CLIENT_RESPONSE));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, invalidRequest)
            );

            assertEquals(expectedErrorCode.getI(), exception.getExceptionResponse().getIdError());
        }

        private static Stream<Arguments> provideInvalidServiceRequests() {
            return Stream.of(
                    Arguments.of(ServiceFactory.createServiceRequestMissingRequiredFields(), Codigo.EQUIPAMENTO),
                    Arguments.of(ServiceFactory.createServiceRequestWithShortDescription(), Codigo.DESCRICAO),
                    Arguments.of(ServiceFactory.createServiceRequestWithFewWords(), Codigo.DESCRICAO),
                    Arguments.of(ServiceFactory.createServiceRequestWithInvalidHorario(), Codigo.HORARIO)
            );
        }

        @Test
        void cadastrarComClienteNaoExistente_ClientCreatedButServiceCreationFails_ShouldNotReturnCreated() {
            // Arrange
            when(clientService.create(VALID_CLIENT_REQUEST)).thenReturn(ResponseEntity.ok(CLIENT_RESPONSE));

            // Act & Assert
            assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.cadastrarComClienteNaoExistente(VALID_CLIENT_REQUEST, VALID_SERVICE_REQUEST)
            );

            // Verify client was still created despite service failure
            verify(clientService).create(VALID_CLIENT_REQUEST);
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
            ResponseEntity<ServicoResponse> response = serviceService.update(EXISTING_SERVICE_ID, VALID_UPDATE_REQUEST);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
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
        void update_InvalidRequestFields_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoUpdateRequest invalidRequest = ServiceFactory.createInvalidServicoUpdateRequest();

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, invalidRequest)
            );

            assertNotNull(exception.getExceptionResponse().getIdError());
        }

        @Test
        void update_NullClientId_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoUpdateRequest requestWithNullClient = ServiceFactory.createServicoUpdateRequestWithNullClient();

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, requestWithNullClient)
            );

            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
        }

        @Test
        void update_NullTechnicianForNonPendingStatus_ShouldThrowServiceNotValidException() {
            // Arrange
            ServicoUpdateRequest requestWithNullTechnician = ServiceFactory.createServicoUpdateRequestWithNullTechnician();

            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, requestWithNullTechnician)
            );

            assertEquals(Codigo.TECNICO.getI(), exception.getExceptionResponse().getIdError());
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
        void update_InvalidScenarios_ShouldThrowServiceNotValidException(ServicoUpdateRequest invalidRequest, Codigo expectedErrorCode) {
            // Arrange
            when(serviceRepository.findById(EXISTING_SERVICE_ID)).thenReturn(Optional.of(EXISTING_SERVICE));

            // Act & Assert
            ServiceNotValidException exception = assertThrows(
                    ServiceNotValidException.class,
                    () -> serviceService.update(EXISTING_SERVICE_ID, invalidRequest)
            );

            assertEquals(expectedErrorCode.getI(), exception.getExceptionResponse().getIdError());
        }

        private static Stream<Arguments> provideInvalidUpdateScenarios() {
            return Stream.of(
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativeValue(), Codigo.SERVICO),
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativeCommission(), Codigo.SERVICO),
                    Arguments.of(ServiceFactory.createUpdateRequestWithNegativePartsValue(), Codigo.SERVICO),
                    Arguments.of(ServiceFactory.createUpdateRequestWithInvalidDate(), Codigo.DATA)
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
        void deleteListByIds_AllExistingServices_ShouldDeleteAllExistingServices() {
            int id1 = 1, id2 = 2;
            when(serviceRepository.findById(id1)).thenReturn(Optional.of(mock(Service.class)));
            when(serviceRepository.findById(id2)).thenReturn(Optional.empty());

            ResponseEntity<Void> response = serviceService.deleteListByIds(List.of(id1, id2));

            verify(serviceRepository).deleteById(id1);
            verify(serviceRepository, never()).deleteById(id2);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void deleteListByIds_IdsValidos_RetornaOkEDeletaServicos() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);

            when(serviceRepository.findById(1)).thenReturn(Optional.of(mock(Service.class)));
            when(serviceRepository.findById(2)).thenReturn(Optional.of(mock(Service.class)));
            when(serviceRepository.findById(3)).thenReturn(Optional.of(mock(Service.class)));

            // Act
            ResponseEntity<Void> response = serviceService.deleteListByIds(ids);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(serviceRepository).deleteById(1);
            verify(serviceRepository).deleteById(2);
            verify(serviceRepository).deleteById(3);
        }

        @Test
        void deleteListByIds_AlgunsIdsInexistentes_DeletaSomenteIdsExistentes() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);

            when(serviceRepository.findById(1)).thenReturn(Optional.of(mock(Service.class)));
            when(serviceRepository.findById(2)).thenReturn(Optional.empty());
            when(serviceRepository.findById(3)).thenReturn(Optional.of(mock(Service.class)));

            // Act
            ResponseEntity<Void> response = serviceService.deleteListByIds(ids);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(serviceRepository).deleteById(1);
            verify(serviceRepository, never()).deleteById(2);
            verify(serviceRepository).deleteById(3);
        }

        @Test
        void deleteListByIds_ListaVazia_NaoChamaDeleteERetornaOk() {
            // Arrange
            List<Integer> ids = Collections.emptyList();

            // Act
            ResponseEntity<Void> response = serviceService.deleteListByIds(ids);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(serviceRepository, never()).deleteById(any());
        }

        @Test
        void deleteListByIds_TodosIdsInexistentes_NaoChamaDeleteMasRetornaOk() {
            // Arrange
            List<Integer> ids = List.of(10, 20, 30);

            when(serviceRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act
            ResponseEntity<Void> response = serviceService.deleteListByIds(ids);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(serviceRepository, never()).deleteById(any());
        }
    }
}