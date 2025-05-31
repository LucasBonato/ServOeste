package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.exceptions.client.ClientNotFoundException;
import com.serv.oeste.application.exceptions.client.ClientNotValidException;
import com.serv.oeste.domain.contracts.repositories.IClientRepository;
import com.serv.oeste.domain.contracts.repositories.IServiceRepository;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.enums.Codigo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

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
            Client client = new Client(
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
            ResponseEntity<ClienteResponse> response = clientService.fetchOneById(1);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(clientRepository).findById(1);

            ClienteResponse clienteResponse = response.getBody();

            assertNotNull(clienteResponse);
            assertEquals(client.getId(), clienteResponse.id());
            assertEquals(client.getNome(), clienteResponse.nome());
            assertEquals(client.getTelefoneFixo(), clienteResponse.telefoneFixo());
            assertEquals(client.getTelefoneCelular(), clienteResponse.telefoneCelular());
            assertEquals(client.getEndereco(), clienteResponse.endereco());
            assertEquals(client.getBairro(), clienteResponse.bairro());
            assertEquals(client.getMunicipio(), clienteResponse.municipio());
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
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Cliente não encontrado!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
        }
    }

    @Nested
    class Create {
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

            Client client = new Client(
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
            ResponseEntity<ClienteResponse> response = clientService.create(request);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(client.getId(), response.getBody().id());
            assertEquals(client.getNome(), response.getBody().nome());
            assertEquals(client.getTelefoneCelular(), response.getBody().telefoneCelular());
            assertEquals(client.getTelefoneFixo(), response.getBody().telefoneFixo());
            assertEquals(client.getEndereco(), response.getBody().endereco());
            assertEquals(client.getBairro(), response.getBody().bairro());
            assertEquals(client.getMunicipio(), response.getBody().municipio());

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

            // Act
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Nome do cliente não pode ser vazio!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Nome do cliente precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Digite Nome e Sobrenome!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Sobrenome do cliente precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.NOMESOBRENOME.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O cliente precisa ter no mínimo um telefone cadastrado!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONES.getI(), exception.getExceptionResponse().getIdError());
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestLengthCellPhone_ShouldThrowClientNotValidException() {
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Telefone celular inválido!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONECELULAR.getI(), exception.getExceptionResponse().getIdError());
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_InvalidRequestLengthLandLine_ShouldThrowClientNotValidException() {
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Telefone fixo inválido!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.TELEFONEFIXO.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Endereço é obrigatório!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.ENDERECO.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("É necessário possuir número no Endereço!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.ENDERECO.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Bairro é obrigatório!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.BAIRRO.getI(), exception.getExceptionResponse().getIdError());
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
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.create(request)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O Município é obrigatório!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.MUNICIPIO.getI(), exception.getExceptionResponse().getIdError());
            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    class Update {
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

            Client client = new Client(
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
            ResponseEntity<ClienteResponse> response = clientService.update(id, request);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(id, response.getBody().id());
            assertEquals("João Silva Pereira", response.getBody().nome());
            assertEquals("11942368296", response.getBody().telefoneCelular());
            assertEquals("1198762345", response.getBody().telefoneFixo());
            assertEquals("Rua Qualquer Coisa, 275", response.getBody().endereco());
            assertEquals("Bairro Qualquer", response.getBody().bairro());
            assertEquals("São Paulo", response.getBody().municipio());

            verify(clientRepository).findById(id);
            verify(clientRepository).save(any(Client.class));
        }
    }

    @Nested
    class DeleteListByIds {
        @Test
        void deleteListByIds_AllClientsWithoutServices_ShouldDeleteAllClients() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);
            ids.forEach(id -> {
                when(clientRepository.findById(id)).thenReturn(Optional.of(new Client()));
                when(serviceRepository.existsByClienteId(id)).thenReturn(false);
            });

            // Act
            ResponseEntity<Void> response = clientService.deleteListByIds(ids);

            // Assert
            verify(clientRepository, times(3)).deleteById(any());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void deleteListByIds_SomeClientsWithServices_ShouldThrowClientNotValidException() {
            // Arrange
            List<Integer> ids = List.of(1, 2, 3);
            when(clientRepository.findById(1)).thenReturn(Optional.of(new Client()));
            when(serviceRepository.existsByClienteId(1)).thenReturn(false);

            when(clientRepository.findById(2)).thenReturn(Optional.of(new Client()));
            when(serviceRepository.existsByClienteId(2)).thenReturn(true);

            when(clientRepository.findById(3)).thenReturn(Optional.of(new Client()));
            when(serviceRepository.existsByClienteId(3)).thenReturn(false);

            // Act
            ClientNotValidException exception = assertThrows(
                    ClientNotValidException.class,
                    () -> clientService.deleteListByIds(ids)
            );

            // Assert
            assertTrue(exception.getExceptionResponse().getMessage().contains("2"));
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("O(s) seguinte(s) cliente(s) não foram excluído(s) por possuirem serviços atrelados a si: [2]", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
            verify(clientRepository, never()).deleteById(2);
        }

        @Test
        void deleteListByIds_SomeClientsNotFound_ShouldIgnoreMissingAndDeleteFound() {
            // Arrange
            List<Integer> ids = List.of(1, 2);
            when(clientRepository.findById(1)).thenReturn(Optional.empty());
            when(clientRepository.findById(2)).thenReturn(Optional.of(new Client()));

            when(serviceRepository.existsByClienteId(2)).thenReturn(false);

            // Act
            ResponseEntity<Void> response = clientService.deleteListByIds(ids);

            // Assert
            verify(clientRepository, never()).deleteById(1);
            verify(clientRepository, times(1)).deleteById(2);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}