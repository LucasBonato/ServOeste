package com.serv.oeste.domain.entities;

import com.serv.oeste.domain.exceptions.specialty.SpecialtyNotValidException;
import com.serv.oeste.domain.valueObjects.Specialty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    void create_ValidName_ShouldCreateActiveSpecialtyWithoutId() {
        Specialty specialty = Specialty.create("Geladeira");

        assertNull(specialty.id());
        assertEquals("Geladeira", specialty.conhecimento());
        assertTrue(specialty.isAtivo());
        assertFalse(specialty.isOutros());
    }

    @Test
    void create_BlankName_ShouldThrowSpecialtyNotValidException() {
        assertThrows(
                SpecialtyNotValidException.class,
                () -> Specialty.create("  ")
        );
        assertThrows(
                SpecialtyNotValidException.class,
                () -> Specialty.create(null)
        );
    }

    @Test
    void create_ShortName_ShouldThrowSpecialtyNotValidException() {
        assertThrows(
                SpecialtyNotValidException.class,
                () -> Specialty.create("A")
        );
    }

    @Test
    void rename_ValidName_ShouldUpdateConhecimento() {
        Specialty specialty = Specialty.create("Geladeira");

        specialty.rename("Geladeira Frost Free");

        assertEquals("Geladeira Frost Free", specialty.conhecimento());
    }

    @Test
    void rename_InvalidName_ShouldThrowSpecialtyNotValidException() {
        Specialty specialty = Specialty.create("Geladeira");

        assertThrows(
                SpecialtyNotValidException.class,
                () -> specialty.rename("")
        );
    }

    @Test
    void deactivate_ShouldSetInactive() {
        Specialty specialty = Specialty.create("Microondas");

        specialty.deactivate();

        assertFalse(specialty.isAtivo());
    }

    @Test
    void activate_ShouldSetActive() {
        Specialty specialty = Specialty.restore(1, "Microondas", false);

        specialty.activate();

        assertTrue(specialty.isAtivo());
    }

    @Test
    void restore_ShouldRecreateWithGivenState() {
        Specialty specialty = Specialty.restore(7, "Lava Louça", false);

        assertEquals(7, specialty.id());
        assertEquals("Lava Louça", specialty.conhecimento());
        assertFalse(specialty.isAtivo());
    }

    @Test
    void isOutros_ShouldReturnTrueOnlyForOutros() {
        Specialty outros = Specialty.create("Outros");
        Specialty microondas = Specialty.create("Microondas");

        assertTrue(outros.isOutros());
        assertFalse(microondas.isOutros());
    }

    @Test
    void legacyConstructor_ShouldCreateActiveSpecialty() {
        Specialty specialty = new Specialty(2, "Bebedouro");

        assertEquals(2, specialty.id());
        assertEquals("Bebedouro", specialty.conhecimento());
        assertTrue(specialty.isAtivo());
        assertThat(specialty)
                .extracting("conhecimento")
                .isEqualTo("Bebedouro");
    }
}