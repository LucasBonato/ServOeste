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
        void fetchOneById_ClientDoNotExists_ShouldThrowClientNotFoundException() {
            // Arrange
            when(clientRepository.findById(1)).thenReturn(Optional.empty());

            // Act
            ClientNotFoundException exception = assertThrows(
                    ClientNotFoundException.class,
                    () -> clientService.fetchOneById(1)
            );

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Cliente não encontrado!", exception.getExceptionResponse().getMessage());
            assertEquals(Codigo.CLIENTE.getI(), exception.getExceptionResponse().getIdError());
        }
    }
}