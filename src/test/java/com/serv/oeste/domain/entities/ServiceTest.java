package com.serv.oeste.domain.entities;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.*;
import com.serv.oeste.domain.exceptions.service.ServiceNotValidException;
import com.serv.oeste.factories.ClientFactory;
import com.serv.oeste.factories.TechnicianFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private final Technician validTechnician = TechnicianFactory.createDefault();
    private final Client validClient = ClientFactory.createDefault();

    @Nested
    class Create {
        @Test
        void create_ValidData_ShouldCreateServiceSuccessfully() {
            // Arrange
            Client client = validClient;
            Technician technician = validTechnician;
            LocalDate dataAtendimento = LocalDate.now().plusDays(2);

            // Act
            Service service = Service.create(
                    "Geladeira",
                    "Brastemp",
                    "Filial 1",
                    "Geladeira não está gelando direito",
                    HorarioPrevisto.MANHA,
                    dataAtendimento,
                    client,
                    technician
            );

            // Assert
            assertNotNull(service);
            assertEquals("Geladeira", service.getEquipamento());
            assertEquals(SituacaoServico.AGUARDANDO_ATENDIMENTO, service.getSituacao());
            assertEquals(client, service.getCliente());
            assertEquals(technician, service.getTecnico());
            assertTrue(service.getDescricao().contains("ABERTURA"));
        }

        @Test
        void create_MissingClient_ShouldThrowServiceNotValidException() {
            // Arrange
            Technician technician = TechnicianFactory.createDefault();

            // Act & Assert
            assertThrows(ServiceNotValidException.class, () ->
                    Service.create(
                            "Televisão",
                            "Samsung",
                            "Filial 1",
                            "Televisão não liga mais",
                            HorarioPrevisto.TARDE,
                            LocalDate.now(),
                            null,
                            technician
                    )
            );
        }

        @Test
        void create_MissingTechnicianButAgendamentoRequired_ShouldInferCorrectSituacao() {
            // Arrange & Act
            Service service = Service.create(
                    "Fogão",
                    "Consul",
                    "Filial 2",
                    "Chamas apagando sozinhas",
                    null,
                    null,
                    validClient,
                    null
            );

            // Assert
            assertEquals(SituacaoServico.AGUARDANDO_AGENDAMENTO, service.getSituacao());
            assertNull(service.getTecnico());
        }

        @Test
        void create_InvalidDescriptionTooShort_ShouldThrowServiceNotValidException() {
            // Arrange
            Client client = validClient;
            Technician technician = validTechnician;

            // Act & Assert
            assertThrows(ServiceNotValidException.class, () ->
                    Service.create(
                            "Geladeira",
                            "Brastemp",
                            "Filial 1",
                            "Pequeno",
                            HorarioPrevisto.MANHA,
                            LocalDate.now(),
                            client,
                            technician
                    )
            );
        }

        @Test
        void create_DescriptionTooFewWords_ShouldThrowServiceNotValidException() {
            // Arrange
            Client client = validClient;
            Technician technician = validTechnician;

            // Act & Assert
            assertThrows(ServiceNotValidException.class, () ->
                    Service.create(
                            "Fogão",
                            "Consul",
                            "Filial 1",
                            "Falha ignição",
                            HorarioPrevisto.MANHA,
                            LocalDate.now(),
                            client,
                            technician
                    )
            );
        }
    }

    @Nested
    class Update {
        @Test
        void update_ValidData_ShouldUpdateSuccessfully() {
            // Arrange
            Client client = validClient;
            Technician technician = validTechnician;

            Service service = Service.create(
                    "Geladeira",
                    "Brastemp",
                    "Filial 1",
                    "Geladeira parou de funcionar",
                    HorarioPrevisto.MANHA,
                    LocalDate.now(),
                    client,
                    technician
            );

            String newDescricao = "Trocado compressor e feito testes de vedação";
            LocalDate fechamento = LocalDate.now();

            // Act
            service.update(
                    "Geladeira",
                    "Brastemp",
                    "Filial 1",
                    newDescricao,
                    SituacaoServico.RESOLVIDO,
                    HorarioPrevisto.TARDE,
                    200.0,
                    FormaPagamento.PIX,
                    50.0,
                    30.0,
                    LocalDate.now(),
                    fechamento,
                    LocalDate.now(),
                    LocalDate.now().plusMonths(6),
                    LocalDate.now(),
                    LocalDate.now(),
                    client,
                    technician
            );

            // Assert
            assertEquals(SituacaoServico.RESOLVIDO, service.getSituacao());
            assertTrue(service.getDescricao().contains(SituacaoServico.RESOLVIDO.getSituacao().toUpperCase()));
        }

        @Test
        void update_RemoveTechnicianWhenNeeded_ShouldThrowServiceNotValidException() {
            // Arrange
            Client client = validClient;

            Service service = Service.create(
                    "Microondas",
                    "Electrolux",
                    "Filial 2",
                    "Problema de aquecimento no magnetron",
                    HorarioPrevisto.MANHA,
                    LocalDate.now(),
                    client,
                    validTechnician
            );

            // Act & Assert
            assertThrows(ServiceNotValidException.class, () ->
                    service.update(
                            "Microondas",
                            "Electrolux",
                            "Filial 2",
                            "Tentativa de atualizar sem técnico",
                            SituacaoServico.AGUARDANDO_ATENDIMENTO,
                            HorarioPrevisto.TARDE,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            LocalDate.now(),
                            LocalDate.now(),
                            client,
                            null
                    )
            );
        }
    }

    @Nested
    class GetHasGarantia {
        @Test
        void getHasGarantia_WithinWarrantyPeriod_ShouldReturnTrue() {
            // Arrange

            LocalDate now = LocalDate.now();
            Service service = Service.restore(
                    1,
                    "TV",
                    "LG",
                    "Filial 1",
                    "Troca de painel principal",
                    SituacaoServico.RESOLVIDO,
                    HorarioPrevisto.TARDE,
                    200.0,
                    FormaPagamento.PIX,
                    30.0,
                    20.0,
                    now,
                    now.minusDays(10),
                    now,
                    now.minusDays(5),
                    now.plusDays(10),
                    now,
                    null,
                    validClient,
                    validTechnician
            );

            // Act
            Boolean hasGarantia = service.getHasGarantia();

            // Assert
            assertTrue(hasGarantia);
        }

        @Test
        void getHasGarantia_ExpiredWarranty_ShouldReturnFalse() {
            // Arrange

            LocalDate now = LocalDate.now();
            Service service = Service.restore(
                    1,
                    "TV",
                    "LG",
                    "Filial 1",
                    "Troca de painel principal",
                    SituacaoServico.RESOLVIDO,
                    HorarioPrevisto.TARDE,
                    200.0,
                    FormaPagamento.PIX,
                    30.0,
                    20.0,
                    now,
                    now.minusMonths(2),
                    now.minusMonths(1),
                    now.minusMonths(2),
                    now.minusMonths(1),
                    now,
                    null,
                    validClient,
                    validTechnician
            );

            // Act
            Boolean hasGarantia = service.getHasGarantia();

            // Assert
            assertFalse(hasGarantia);
        }

        @Test
        void getHasGarantia_NoWarrantyDates_ShouldReturnNull() {
            // Arrange

            Service service = Service.restore(
                    1,
                    "TV",
                    "LG",
                    "Filial 1",
                    "Troca de painel principal",
                    SituacaoServico.RESOLVIDO,
                    HorarioPrevisto.TARDE,
                    200.0,
                    FormaPagamento.PIX,
                    30.0,
                    20.0,
                    null,
                    LocalDate.now(),
                    LocalDate.now(),
                    null,
                    null,
                    LocalDate.now(),
                    null,
                    validClient,
                    validTechnician
            );

            // Act
            Boolean hasGarantia = service.getHasGarantia();

            // Assert
            assertNull(hasGarantia);
        }
    }

    @Nested
    class Restore {
        @Test
        void restore_ValidData_ShouldBypassValidation() {
            // Arrange & Act
            Service service = Service.restore(
                    1,
                    "Geladeira",
                    "Brastemp",
                    "Filial 1",
                    "Histórico antigo de descrição",
                    SituacaoServico.RESOLVIDO,
                    HorarioPrevisto.MANHA,
                    100.0,
                    FormaPagamento.PIX,
                    10.0,
                    20.0,
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    null,
                    validClient,
                    null
            );

            // Assert
            assertNotNull(service);
            assertEquals(1, service.getId());
            assertEquals("Geladeira", service.getEquipamento());
        }
    }
}
