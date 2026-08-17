package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.TechnicianAvailabilityProjection;
import com.serv.oeste.domain.valueObjects.TechnicianAvailability;
import com.serv.oeste.infrastructure.repositories.jpa.ITechnicianJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianRepositoryTest {
    @Mock private ITechnicianJpaRepository technicianJpaRepository;
    @Mock private EntityManager entityManager;

    private TechnicianAvailabilityProjection mockProjection(
            Integer id,
            String nome,
            LocalDate data,
            Integer dia,
            String periodo,
            Integer quantidade
    ) {
        TechnicianAvailabilityProjection projection = mock(TechnicianAvailabilityProjection.class);
        when(projection.getId()).thenReturn(id);
        when(projection.getNome()).thenReturn(nome);
        when(projection.getData()).thenReturn(data);
        when(projection.getDia()).thenReturn(dia);
        when(projection.getPeriodo()).thenReturn(periodo);
        when(projection.getQuantidade()).thenReturn(quantidade);
        return projection;
    }

    @Test
    void getTechnicianAvailabilityBySpecialty_ProjectionsWithLocalDateData_ShouldMapAllFieldsPreservingLocalDate() {
        // Arrange
        TechnicianRepository repository = new TechnicianRepository(technicianJpaRepository, entityManager);
        LocalDate data = LocalDate.of(2026, 8, 14);

        TechnicianAvailabilityProjection joaoProjection = mockProjection(1, "João Silva", data, 5, "MANHÃ", 2);
        TechnicianAvailabilityProjection mariaProjection = mockProjection(2, "Maria Souza", data, 5, "TARDE", 0);

        when(technicianJpaRepository.getTechnicianAvailabilityBySpecialty(3, 1))
                .thenReturn(List.of(joaoProjection, mariaProjection));

        // Act
        List<TechnicianAvailability> result = repository.getTechnicianAvailabilityBySpecialty(3, 1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        TechnicianAvailability joao = result.get(0);
        assertEquals(1, joao.id());
        assertEquals("João Silva", joao.nome());
        assertEquals(data, joao.data());
        assertEquals(5, joao.dia());
        assertEquals("MANHÃ", joao.periodo());
        assertEquals(2, joao.quantidade());

        TechnicianAvailability maria = result.get(1);
        assertEquals(2, maria.id());
        assertEquals("Maria Souza", maria.nome());
        assertEquals(data, maria.data());
        assertEquals(5, maria.dia());
        assertEquals("TARDE", maria.periodo());
        assertEquals(0, maria.quantidade());

        verify(technicianJpaRepository).getTechnicianAvailabilityBySpecialty(3, 1);
    }

    @Test
    void getTechnicianAvailabilityBySpecialty_NoResults_ShouldReturnEmptyList() {
        // Arrange
        TechnicianRepository repository = new TechnicianRepository(technicianJpaRepository, entityManager);

        when(technicianJpaRepository.getTechnicianAvailabilityBySpecialty(anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        // Act
        List<TechnicianAvailability> result = repository.getTechnicianAvailabilityBySpecialty(4, 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTechnicianAvailabilityBySpecialty_ProjectionWithNullFields_ShouldMapNullValues() {
        // Arrange
        TechnicianRepository repository = new TechnicianRepository(technicianJpaRepository, entityManager);

        TechnicianAvailabilityProjection projection = mockProjection(
                1,
                "João Silva",
                LocalDate.of(2026, 8, 14),
                5,
                null,
                null
        );

        when(technicianJpaRepository.getTechnicianAvailabilityBySpecialty(3, 1))
                .thenReturn(List.of(projection));

        // Act
        List<TechnicianAvailability> result = repository.getTechnicianAvailabilityBySpecialty(3, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.getFirst().periodo());
        assertNull(result.getFirst().quantidade());
        assertEquals(LocalDate.of(2026, 8, 14), result.getFirst().data());
    }
}
