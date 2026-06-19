package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InternDTOTest {

    private InternDTO testIntern;

    @BeforeEach
    void setUpEach() {
        testIntern = new InternDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con matrícula nula")
    void testSetStudentNumberFailedNull() {
        assertThrows(IllegalArgumentException.class, () -> testIntern.setStudentNumber(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con matrícula de formato inválido")
    void testSetStudentNumberFailedFormat() {
        assertThrows(IllegalArgumentException.class, () -> testIntern.setStudentNumber("MATRICULA123"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con sexo nulo")
    void testSetSexFailedNull() {
        assertThrows(IllegalArgumentException.class, () -> testIntern.setSex(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con fecha de nacimiento futura")
    void testSetBirthDateFailedFuture() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);
        assertThrows(IllegalArgumentException.class, () -> testIntern.setBirthDate(futureDate));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con fecha de nacimiento mayor a 100 años")
    void testSetBirthDateFailedTooOld() {
        LocalDateTime ancientDate = LocalDateTime.now().minusYears(101);
        assertThrows(IllegalArgumentException.class, () -> testIntern.setBirthDate(ancientDate));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos para el practicante")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testIntern.setStudentNumber("S24013315");
            testIntern.setSex("Masculino");
            testIntern.setBirthDate(LocalDateTime.now().minusYears(20));
        });
    }
}