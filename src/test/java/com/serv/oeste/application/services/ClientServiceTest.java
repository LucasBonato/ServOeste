package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.client.ClientNotFoundException;
import com.serv.oeste.domain.exceptions.client.ClientNotValidException;
import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.exceptions.valueObjects.PhoneNotValidException;
import com.serv.oeste.domain.valueObjects.ClientFilter;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock private IClientRepository clientRepository;
    @Mock private IServiceRepository serviceRepository;
    @InjectMocks private ClientService clientService;

    // MethodName_StateUnderTest_ExpectedBehavior

    @Nested
    class FetchOneById {
        @Test
        void fetchOneById_ClientExists_ShouldReturnClientWithSuccess() {
            // Arrange
            Client client = Client.restore(
                    1,
                    "Rafael Montes da Silva",
                    "1178474208",
                    "11962057189",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            when(clientRepository.findById(1)).thenReturn(Optional.of(client));

            // Act
            ClienteResponse response = clientService.fetchOneById(1);

            // Assert
            verify(clientRepository).findById(1);

            assertNotNull(response);
            assertEquals(client.getId(), response.id());
            assertEquals(client.getNome(), response.nome());
            assertEquals(client.getTelefoneFixo(), response.telefoneFixo());
            assertEquals(client.getTelefoneCelular(), response.telefoneCelular());
            assertEquals(client.getEndereco(), response.endereco());
            assertEquals(client.getBairro(), response.bairro());
            assertEquals(client.getMunicipio(), response.municipio());
        }

        @Test
        void fetchOneById_ClientDoesNotExists_ShouldThrowClientNotFoundException() {
            // Arrange
            when(clientRepository.findById(1)).thenReturn(Optional.empty());

            // Act
            ClientNotFoundException exception = assertThrows(
                    ClientNotFoundException.class,
                    () -> clientService.fetchOneById(1)
            );

            // Assert
            assertTrue(exception.getMessage().contains("Cliente não encontrado!"));
            assertTrue(exception.getKeys().contains(ErrorFields.CLIENTE.getFieldName()));
        }
    }

    @Nested
    class FetchListByFilter {
        final Client JOAO = Client.restore(1, "João Silva Pereira", "1176452476", "11946289576", "Rua Alguma Coisa", "Bairroso", "São Paulo");
        final Client MARIA = Client.restore(2, "Maria Eduarda Ferreira", "1198762345", "11909871234", "Rua Alguma da Silva", "Bairro Diferenciado", "São Paulo");

        @Test
        void fetchListByFilter_NoFilter_ShouldReturnAllClients() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter(null, null, null);
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter))
                    .thenReturn(new PageResponse<>(
                            List.of(JOAO, MARIA),
                            1,
                            0,
                            10
                        )
                    );

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ClienteResponse> body = response.getContent();
            assertNotNull(body);
            assertEquals(2, body.size());
            assertTrue(body.stream().anyMatch(c -> c.id().equals(JOAO.getId())));
            assertTrue(body.stream().anyMatch(c -> c.id().equals(MARIA.getId())));
        }

        @Test
        void fetchListByFilter_FilterByNome_ShouldReturnMatchingClients() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter("Maria", null, null);
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(MARIA),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ClienteResponse> body = response.getContent();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(MARIA.getId(), body.getFirst().id());
        }

        @Test
        void fetchListByFilter_FilterByTelefone_ShouldReturnClientsWithMatchingPhones() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter(null, "1194628", null);
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(JOAO),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ClienteResponse> body = response.getContent();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(JOAO.getId(), body.getFirst().id());
        }

        @Test
        void fetchListByFilter_FilterByEndereco_ShouldReturnClientsWithMatchingAddress() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter(null, null, "Silva");
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(MARIA),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ClienteResponse> body = response.getContent();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(MARIA.getId(), body.getFirst().id());
        }

        @Test
        void fetchListByFilter_AllFieldsMatch_ShouldReturnCorrectClient() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter("João", "1194628", "Rua Alguma");
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    List.of(JOAO),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            List<ClienteResponse> body = response.getContent();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(JOAO.getId(), body.getFirst().id());
        }

        @Test
        void fetchListByFilter_NoMatch_ShouldReturnEmptyList() {
            // Arrange
            ClienteRequestFilter filterRequest = new ClienteRequestFilter("Inexistente", null, null);
            ClientFilter filter = filterRequest.toClientFilter();
            PageFilterRequest pageFilterRequest = new PageFilterRequest(10, 0);
            PageFilter pageFilter = pageFilterRequest.toPageFilter();

            when(clientRepository.filter(filter, pageFilter)).thenReturn(new PageResponse<>(
                    Collections.emptyList(),
                    1,
                    0,
                    10
            ));

            // Act
            PageResponse<ClienteResponse> response = clientService.fetchListByFilter(filterRequest, pageFilterRequest);

            // Assert
            assertNotNull(response);
            assertTrue(response.getContent().isEmpty());
        }
    }

    @Nested
    class Create {
        @Test
        void create_ValidRequest_ShouldSaveClientAndReturnResponse() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );
            Client client = Client.restore(
                    1,
                    "João Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );
            when(clientRepository.save(any(Client.class))).thenReturn(client);

            // Act
            ClienteResponse response = clientService.create(request);

            // Assert
            verify(clientRepository).save(any(Client.class));
            assertNotNull(response);
            assertEquals(client.getId(), response.id());
            assertEquals(client.getNome(), response.nome());
        }

        @Test
        void create_WhenRepositoryThrows_ShouldPropagateException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );
            when(clientRepository.save(any(Client.class)))
                    .thenThrow(new RuntimeException("Database down"));

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> clientService.create(request)
            );

            assertEquals("Database down", exception.getMessage());
        }

        @Test
        void create_ValidRequest_ShouldCreateClientSuccessfully() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            Client client = Client.restore(
                    1,
                    "João Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            when(clientRepository.save(any(Client.class))).thenReturn(client);

            // Act
            ClienteResponse response = clientService.create(request);

            // Assert
            assertNotNull(response);
            assertEquals(client.getId(), response.id());
            assertEquals(client.getNome(), response.nome());
            assertEquals(client.getTelefoneCelular(), response.telefoneCelular());
            assertEquals(client.getTelefoneFixo(), response.telefoneFixo());
            assertEquals(client.getEndereco(), response.endereco());
            assertEquals(client.getBairro(), response.bairro());
            assertEquals(client.getMunicipio(), response.municipio());

            verify(clientRepository).save(any(Client.class));
        }

        @Test
        void create_InvalidRequestWithBlankName_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act & Assert
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithShortName_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "A",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithBlankSurname_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithShortSurname_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "a",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithNoPhone_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "",
                    "",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestLengthCellPhone_ShouldThrowPhoneNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "",
                    "11942623746238",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    PhoneNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestLengthLandLine_ShouldThrowPhoneNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1192",
                    "",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    PhoneNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithBlankAddress_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithNoNumberAddress_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithBlankDistrict_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 234",
                    "",
                    "São Paulo"
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestWithBlankMunicipality_ShouldThrowClientNotValidException() {
            // Arrange
            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 244",
                    "Bairro Qualquer",
                    ""
            );

            // Act
            assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    class Update {
        @Test
        void update_ExistingClient_ShouldUpdateAndSaveSuccessfully() {
            // Arrange
            Integer id = 1;
            ClienteRequest request = new ClienteRequest(
                    "João", "Silva Pereira",
                    "1198762345", "11942368296",
                    "Rua Qualquer Coisa, 275", "Bairro Qualquer", "São Paulo"
            );
            Client existing = Client.restore(
                    id, "Old Name", "1111111111", "2222222222",
                    "Old Street", "Old Bairro", "Old City"
            );

            when(clientRepository.findById(id)).thenReturn(Optional.of(existing));
            when(clientRepository.save(any(Client.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            ClienteResponse response = clientService.update(id, request);

            // Assert
            verify(clientRepository).findById(id);
            verify(clientRepository).save(any(Client.class));
            assertEquals("João Silva Pereira", response.nome());
            assertEquals("1198762345", response.telefoneFixo());
        }

        @Test
        void update_ClientNotFound_ShouldThrowException() {
            // Arrange
            Integer id = 999;
            when(clientRepository.findById(id)).thenReturn(Optional.empty());

            ClienteRequest request = new ClienteRequest(
                    "João", "Silva Pereira",
                    "1198762345", "11942368296",
                    "Rua Qualquer Coisa, 275", "Bairro Qualquer", "São Paulo"
            );

            // Act & Assert
            assertThrows(ClientNotFoundException.class, () -> clientService.update(id, request));
            verify(clientRepository, never()).save(any());
        }

        @Test
        void update_ValidRequestWithExistingClient_ShouldUpdateClientSuccessfully() {
            // Arrange
            Integer id = 1;

            ClienteRequest request = new ClienteRequest(
                    "João",
                    "Silva Pereira",
                    "1198762345",
                    "11942368296",
                    "Rua Qualquer Coisa, 275",
                    "Bairro Qualquer",
                    "São Paulo"
            );

            Client client = Client.restore(
                    1,
                    "NomeAntigo + SobrenomeAntigo",
                    "1112341234",
                    "11989765327",
                    "Rua Antiga, 123",
                    "Bairro Antigo",
                    "Municipio Antigo"
            );

            when(clientRepository.findById(id)).thenReturn(Optional.of(client));
            when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            ClienteResponse response = clientService.update(id, request);

            // Assert
            assertNotNull(response);
            assertEquals(id, response.id());
            assertEquals("João Silva Pereira", response.nome());
            assertEquals("11942368296", response.telefoneCelular());
            assertEquals("1198762345", response.telefoneFixo());
            assertEquals("Rua Qualquer Coisa, 275", response.endereco());
            assertEquals("Bairro Qualquer", response.bairro());
            assertEquals("São Paulo", response.municipio());

            verify(clientRepository).findById(id);
            verify(clientRepository).save(any(Client.class));
        }
    }

    @Nested
    class DeleteListByIds {
        @Test
        void deleteListByIds_ClientsWithoutServices_ShouldDeleteSuccessfully() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);
            List<Client> clients = ids.stream()
                    .map(id -> Client.restore(id, "Client " + id, "1111111111", "1199999999",
                            "Rua Teste, 1", "Bairro", "Cidade"))
                    .toList();

            when(clientRepository.findAllByIds(ids)).thenReturn(clients);
            when(serviceRepository.findAllClientIdsWithServices(ids)).thenReturn(Set.of());

            // Act
            clientService.deleteListByIds(ids);

            // Assert
            verify(clientRepository).deleteAllByIds(ids);
        }

        @Test
        void deleteListByIds_ClientsWithServices_ShouldThrowClientNotValidException() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);
            when(clientRepository.findAllByIds(ids)).thenReturn(
                    ids.stream()
                            .map(id -> Client.restore(id, "Client " + id, "1111111111", "1199999999",
                                    "Rua Teste, 1", "Bairro", "Cidade"))
                            .toList()
            );
            when(serviceRepository.findAllClientIdsWithServices(ids)).thenReturn(Set.of(2));

            // Act & Assert
            ClientNotValidException ex = assertThrows(ClientNotValidException.class, () ->
                    clientService.deleteListByIds(ids)
            );

            assertTrue(ex.getMessage().contains("2"));
            verify(clientRepository, never()).deleteAllByIds(any());
        }
    }
}