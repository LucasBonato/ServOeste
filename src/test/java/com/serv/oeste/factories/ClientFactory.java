package com.serv.oeste.factories;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.domain.entities.client.Client;

public class ClientFactory {
    // Default instances
    public static Client createDefault() {
        return Client.restore(
                1,
                "João Silva",
                "1122223333",
                "11999998888",
                "Rua das Flores, 123",
                "Centro",
                "São Paulo"
        );
    }

    public static Client createWithId(int id) {
        return Client.restore(
                id,
                "João Silva",
                "1122223333",
                "11999998888",
                "Rua das Flores, 123",
                "Centro",
                "São Paulo"
        );
    }

    public static Client createWithSobrenome() {
        return Client.create(
                "Maria",
                "Oliveira",
                "2123456789",
                "21912345678",
                "Av. Atlântica, 456",
                "Copacabana",
                "Rio de Janeiro"
        );
    }

    // For ClienteRequest scenarios
    public static ClienteRequest createValidClienteRequest() {
        return new ClienteRequest(
                "Carlos",
                "Santos",
                "1133334444",
                "11988887777",
                "Rua das Palmeiras, 789",
                "Jardins",
                "São Paulo"
        );
    }

    public static ClienteRequest createClienteRequestMissingRequiredFields() {
        return new ClienteRequest(
                null,  // Missing first name
                null,  // Missing last name
                null,  // Missing phone
                null,  // Missing cell phone
                null,  // Missing address
                null,  // Missing neighborhood
                null   // Missing city
        );
    }

    public static ClienteRequest createClienteRequestWithInvalidPhone() {
        return new ClienteRequest(
                "Ana",
                "Silveira",
                "123",  // Invalid phone
                "456",   // Invalid cell phone
                "Av. Paulista, 1000",
                "Bela Vista",
                "São Paulo"
        );
    }

    // For ClienteResponse scenarios
    public static ClienteResponse createValidClienteResponse() {
        return new ClienteResponse(createDefault());
    }

    public static ClienteResponse createClienteResponseWithId(Integer id) {
        Client client = createWithId(id);
        return new ClienteResponse(client);
    }

    public static ClienteResponse createMinimalClienteResponse(Integer id) {
        return new ClienteResponse(
                id,
                "Minimal Client",
                null,
                null,
                null,
                null,
                null
        );
    }

    // Custom creation methods
    public static Client createCustom(
            Integer id,
            String nome,
            String telefoneFixo,
            String telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        return Client.restore(id, nome, telefoneFixo, telefoneCelular, endereco, bairro, municipio);
    }

    public static Client createCustomWithSobrenome(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        return Client.create(
                nome,
                sobrenome,
                telefoneFixo,
                telefoneCelular,
                endereco,
                bairro,
                municipio
        );
    }

    // Special cases
    public static Client createClientWithMinimalInfo() {
        return Client.restore(
                99,
                "Minimal Client",
                null,
                null,
                null,
                null,
                null
        );
    }

    public static Client createClientWithLongName() {
        return Client.restore(
                100,
                "Nome Muito Longo Que Deve Ser Validado Pelo Sistema Para Verificar Se Caberá No Banco de Dados",
                "1144445555",
                "11977776666",
                "Rua dos Testes, 999",
                "Test Neighborhood",
                "Test City"
        );
    }
}
