package com.serv.oeste.domain.entities;

import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;
import com.serv.oeste.domain.exceptions.technician.TechnicianAlreadyDisabledException;
import com.serv.oeste.domain.exceptions.technician.TechnicianNotValidException;
import com.serv.oeste.domain.valueObjects.Specialty;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TechnicianTest {
    @Nested
    class Create {
        @Test
        void create_ValidData_shouldCreateTechnicianSuccessfully() {
            // Arrange
            String nome = "Alberto";
            String sobrenome = "Rodrigues";
            String fixo = "1133445566";
            String celular = "11988887777";
            List<Specialty> especialidades = List.of(new Specialty(1, "Eletricista"));

            // Act
            Technician technician = Technician.create(nome, sobrenome, fixo, celular, especialidades);

            // Assert
            assertEquals(nome, technician.getNome());
            assertEquals(sobrenome, technician.getSobrenome());
            assertEquals(fixo, technician.getTelefoneFixo());
            assertEquals(celular, technician.getTelefoneCelular());
            assertEquals(1, technician.getEspecialidades().size());
            assertEquals(Situacao.ATIVO, technician.getSituacao());
        }

        @Test
        void create_MissingPhones_shouldThrowWhenNoPhonesProvided() {
            // Arrange
            List<Specialty> especialidades = List.of(new Specialty(1, "Hidráulica"));

            // Act & Assert
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> Technician.create("Alberto", "Rodrigues", null, null, especialidades)
            );
            assertTrue(exception.getMessage().contains("telefone"));
        }

        @Test
        void create_EmptySpecialties_shouldThrowWhenNoSpecialtiesProvided() {
            // Arrange
            List<Specialty> especialidades = List.of();

            // Act & Assert
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> Technician.create("Alberto", "Rodrigues", "11988887777", null, especialidades)
            );
            assertTrue(exception.getMessage().contains("especialidade"));
        }
    }

    @Nested
    class Restore {
        @Test
        void restore_ValidData_shouldRestoreTechnicianSuccessfully() {
            // Arrange
            Integer id = 10;
            String fixo = "1133445566";
            String celular = "11988887777";
            List<Specialty> especialidades = List.of(new Specialty(1, "Mecânica"));

            // Act
            Technician technician = Technician.restore(
                    id,
                    "Alberto",
                    "Rodrigues",
                    fixo,
                    celular,
                    Situacao.LICENCA,
                    especialidades
            );

            // Assert
            assertEquals(id, technician.getId());
            assertEquals(Situacao.LICENCA, technician.getSituacao());
            assertEquals(celular, technician.getTelefoneCelular());
        }
    }

    @Nested
    class Update {
        @Test
        void update_ValidData_shouldUpdateTechnicianSuccessfully() {
            // Arrange
            Technician technician = Technician.create(
                    "Alberto",
                    "Rodrigues",
                    "1133445566",
                    "11988887777",
                    List.of(new Specialty(1, "Elétrica"))
            );

            // Act
            technician.update(
                    "Alberto",
                    "Silva",
                    "1144445566",
                    "11999998888",
                    Situacao.LICENCA,
                    List.of(new Specialty(2, "Mecânica"))
            );

            // Assert
            assertEquals("Silva", technician.getSobrenome());
            assertEquals("1144445566", technician.getTelefoneFixo());
            assertEquals(Situacao.LICENCA, technician.getSituacao());
            assertEquals("Mecânica", technician.getEspecialidades().getFirst().conhecimento());
        }

        @Test
        void update_InvalidData_shouldThrowWhenUpdatingToInvalidData() {
            // Arrange
            Technician technician = Technician.create(
                    "Alberto",
                    "Rodrigues",
                    "11988887777",
                    null,
                    List.of(new Specialty(1, "Elétrica"))
            );

            // Act & Assert
            TechnicianNotValidException exception = assertThrows(
                    TechnicianNotValidException.class,
                    () -> technician.update("Alberto", "Rodrigues", null, null, Situacao.ATIVO, List.of())
            );

            assertTrue(exception.getMessage().contains("telefone"));
        }
    }

    @Nested
    class Disable {
        @Test
        void disable_Situation_shouldDisableTechnicianSuccessfully() {
            // Arrange
            Technician technician = Technician.create(
                    "Alberto",
                    "Rodrigues",
                    "11988887777",
                    null,
                    List.of(new Specialty(1, "Elétrica"))
            );

            // Act
            technician.disable();

            // Assert
            assertEquals(Situacao.DESATIVADO, technician.getSituacao());
        }

        @Test
        void disable_Situation_shouldThrowWhenAlreadyDisabled() {
            // Arrange
            Technician technician = Technician.restore(
                    1,
                    "Alberto",
                    "Rodrigues",
                    "1133445566",
                    null,
                    Situacao.DESATIVADO,
                    List.of(new Specialty(1, "Elétrica"))
            );

            // Act & Assert
            assertThrows(TechnicianAlreadyDisabledException.class, technician::disable);
        }
    }
}