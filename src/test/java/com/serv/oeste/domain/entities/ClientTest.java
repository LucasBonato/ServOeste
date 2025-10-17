package com.serv.oeste.domain.entities;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.exceptions.client.ClientNotValidException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private static final String VALID_FIXO = "1134567890"; // 10 digits
    private static final String VALID_CEL = "11987654321";  // 11 digits
    private static final String VALID_ADDRESS = "Rua A, 123";
    private static final String VALID_BAIRRO = "Centro";
    private static final String VALID_MUNICIPIO = "São Paulo";

    @Nested
    class Create {
        @Test
        void create_ValidData_ShouldCreateClientSuccessfully() {
            // Arrange
            String nome = "Rafael";
            String sobrenome = "Gomes";
            String telefoneFixo = "1122334455";
            String telefoneCelular = "11988887777";
            String endereco = "Rua Central 123";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act
            Client client = Client.create(
                    nome, sobrenome, telefoneFixo, telefoneCelular, endereco, bairro, municipio
            );

            // Assert
            assertNotNull(client);
            assertEquals("Rafael Gomes", client.getNome());
            assertEquals(telefoneFixo, client.getTelefoneFixo());
            assertEquals(telefoneCelular, client.getTelefoneCelular());
            assertEquals(endereco, client.getEndereco());
            assertEquals(bairro, client.getBairro());
            assertEquals(municipio, client.getMunicipio());
        }

        @Test
        void create_NoPhonesProvided_ShouldThrowClientNotValidException() {
            // Arrange
            String nome = "Rafael";
            String sobrenome = "Gomes";
            String endereco = "Rua Central 123";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    Client.create(nome, sobrenome, null, null, endereco, bairro, municipio)
            );
        }

        @Test
        void create_AddressWithoutNumber_ShouldThrowClientNotValidException() {
            // Arrange
            String nome = "Rafael";
            String sobrenome = "Gomes";
            String telefoneCelular = "11988887777";
            String endereco = "Rua Central";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    Client.create(nome, sobrenome, null, telefoneCelular, endereco, bairro, municipio)
            );
        }

        @Test
        void create_BlankName_ShouldThrowClientNotValidException() {
            // Arrange
            String nome = " ";
            String sobrenome = " ";
            String telefoneCelular = "11988887777";
            String endereco = "Rua Central 123";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    Client.create(nome, sobrenome, null, telefoneCelular, endereco, bairro, municipio)
            );
        }

        @Test
        void create_NameTooShort_ShouldThrowClientNotValidException() {
            // Arrange
            String nome = "A";
            String sobrenome = "";
            String telefoneCelular = "11988887777";
            String endereco = "Rua Central 123";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    Client.create(nome, sobrenome, null, telefoneCelular, endereco, bairro, municipio)
            );
        }

        @Test
        void create_BlankAddressBairroMunicipio_ShouldThrowClientNotValidException() {
            // Arrange
            String nome = "Rafael";
            String sobrenome = "Gomes";
            String telefoneCelular = "11988887777";
            String endereco = " ";
            String bairro = " ";
            String municipio = " ";

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    Client.create(nome, sobrenome, null, telefoneCelular, endereco, bairro, municipio)
            );
        }

        @Test
        void create_NoPhonesProvided_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", null, null, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_OnlyInvalidPhones_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", "", " ", VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_AddressWithoutNumber_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", VALID_FIXO, null, "Rua das Flores", VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_AddressIsBlank_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", VALID_FIXO, null, "  ", VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_NameIsBlank_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("", " ", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_NameTooShort_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("A", "", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_NameExactlyTwoCharacters_ShouldBeValid() {
            assertDoesNotThrow(() ->
                    Client.create("Lu", "Ta", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void create_BairroIsBlank_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, " ", VALID_MUNICIPIO)
            );
        }

        @Test
        void create_MunicipioIsBlank_ShouldThrowException() {
            assertThrows(ClientNotValidException.class, () ->
                    Client.create("Rafael", "Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, " ")
            );
        }
    }

    @Nested
    class Update {
        @Test
        void update_ValidData_ShouldUpdateClientSuccessfully() {
            // Arrange
            Client client = Client.create(
                    "Rafael", "Gomes", "1122334455", null, "Rua A 123", "Centro", "São Paulo"
            );

            String novoNome = "Rafael";
            String novoSobrenome = "Pereira";
            String novoTelefoneCelular = "11955556666";
            String novoEndereco = "Avenida Paulista 900";
            String novoBairro = "Bela Vista";
            String novoMunicipio = "São Paulo";

            // Act
            client.update(
                    novoNome, novoSobrenome, null, novoTelefoneCelular, novoEndereco, novoBairro, novoMunicipio
            );

            // Assert
            assertEquals("Rafael Pereira", client.getNome());
            assertEquals(novoTelefoneCelular, client.getTelefoneCelular());
            assertEquals(novoEndereco, client.getEndereco());
            assertEquals(novoBairro, client.getBairro());
        }

        @Test
        void update_RemoveAllPhones_ShouldThrowClientNotValidException() {
            // Arrange
            Client client = Client.create(
                    "Rafael", "Gomes", "1122334455", "11988887777", "Rua A 123", "Centro", "São Paulo"
            );

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    client.update("Rafael", "Gomes", null, null, "Rua A 123", "Centro", "São Paulo")
            );
        }

        @Test
        void update_InvalidAddressFormat_ShouldThrowClientNotValidException() {
            // Arrange
            Client client = Client.create(
                    "Rafael", "Gomes", "1122334455", "11988887777", "Rua A 123", "Centro", "São Paulo"
            );

            // Act & Assert
            assertThrows(ClientNotValidException.class, () ->
                    client.update("Rafael", "Gomes", null, "11988887777", "Rua A", "Centro", "São Paulo")
            );
        }

        @Test
        void update_ValidData_ShouldUpdateSuccessfully() {
            Client client = Client.create("Rafael", "Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO);

            client.update("João", "Silva", VALID_FIXO, VALID_CEL, "Rua Nova, 500", "Centro", "Campinas");

            assertEquals("João Silva", client.getNome());
            assertEquals("Rua Nova, 500", client.getEndereco());
            assertEquals("Campinas", client.getMunicipio());
        }

        @Test
        void update_InvalidAddress_ShouldThrowException() {
            Client client = Client.create("Rafael", "Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO);

            assertThrows(ClientNotValidException.class, () ->
                    client.update("Rafael", "Gomes", VALID_FIXO, VALID_CEL, "Rua sem numero", VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void update_RemoveAllPhones_ShouldThrowException() {
            Client client = Client.create("Rafael", "Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO);

            assertThrows(ClientNotValidException.class, () ->
                    client.update("Rafael", "Gomes", null, null, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }
    }

    @Nested
    class Restore {
        @Test
        void restore_ValidData_ShouldRestoreWithoutValidation() {
            // Arrange
            Integer id = 10;
            String nome = "Rafael Gomes";
            String endereco = "Rua Desformatada";
            String bairro = "Centro";
            String municipio = "São Paulo";

            // Act
            Client client = Client.restore(
                    id,
                    nome,
                    null,
                    null,
                    endereco,
                    bairro,
                    municipio
            );

            // Assert
            assertNotNull(client);
            assertEquals(id, client.getId());
            assertEquals("Rafael Gomes", client.getNome());
            // no validation triggered on restore
        }

        @Test
        void restore_ValidData_ShouldNotThrowException() {
            assertDoesNotThrow(() ->
                    Client.restore(1, "Rafael Gomes", VALID_FIXO, VALID_CEL, VALID_ADDRESS, VALID_BAIRRO, VALID_MUNICIPIO)
            );
        }

        @Test
        void restore_InvalidData_ShouldStillRestoreWithoutValidation() {
            // restore bypasses validate()
            Client client = Client.restore(1, "A", null, null, "Sem numero", "", "");

            assertNotNull(client);
            assertEquals("A", client.getNome());
        }
    }
}