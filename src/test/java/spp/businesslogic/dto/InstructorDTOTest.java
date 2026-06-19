package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstructorDTOTest {

    private InstructorDTO testInstructor;

    @BeforeEach
    void setUpEach() {
        testInstructor = new InstructorDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar número personal con formato inválido (minúsculas)")
    void testSetPersonalNumberFailedFormat() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setPersonalNumber("abc12"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al exceder longitud máxima en número personal (>5)")
    void testSetPersonalNumberFailedExceedsMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setPersonalNumber("ABCDEF"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar turno nulo")
    void testSetShiftFailedNull() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setShift(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al exceder longitud máxima en turno (>45)")
    void testSetShiftFailedExceedsMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setShift("A".repeat(46)));
    }

    @Test
    @DisplayName("Debe aceptar turno con longitud máxima de 45 caracteres")
    void testSetShiftSuccessMaxLength() {
        assertDoesNotThrow(() -> testInstructor.setShift("A".repeat(45)));
    }
}