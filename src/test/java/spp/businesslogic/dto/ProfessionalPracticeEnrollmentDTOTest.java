package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProfessionalPracticeEnrollmentDTOTest {

    private ProfessionalPracticeEnrollmentDTO testEnrollment;

    @BeforeEach
    void setUpEach() {
        testEnrollment = new ProfessionalPracticeEnrollmentDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar Practicante nulo")
    void testSetInternNull() {
        assertThrows(IllegalArgumentException.class, () -> testEnrollment.setInternDTO(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar Experiencia Educativa nula")
    void testSetCourseNull() {
        assertThrows(IllegalArgumentException.class, () -> testEnrollment.setCourseDTO(null));
    }

    @Test
    @DisplayName("Debe aceptar Proyecto nulo (estado inicial)")
    void testSetProjectNullSuccess() {
        assertDoesNotThrow(() -> testEnrollment.setProjectDTO(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con calificación menor a 0")
    void testSetFinalGradeNegative() {
        assertThrows(IllegalArgumentException.class, () -> testEnrollment.setFinalGrade(-5));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con calificación mayor a 100")
    void testSetFinalGradeTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> testEnrollment.setFinalGrade(101));
    }

    @Test
    @DisplayName("Debe aceptar calificación dentro del rango permitido")
    void testSetFinalGradeSuccess() {
        assertDoesNotThrow(() -> testEnrollment.setFinalGrade(85));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con horas cubiertas negativas")
    void testSetCoveredHoursNegative() {
        assertThrows(IllegalArgumentException.class, () -> testEnrollment.setCoveredHours(-10));
    }

    @Test
    @DisplayName("Debe aceptar horas cubiertas iguales a 0")
    void testSetCoveredHoursZero() {
        assertDoesNotThrow(() -> testEnrollment.setCoveredHours(0));
    }

    @Test
    @DisplayName("Debe aceptar horas cubiertas positivas")
    void testSetCoveredHoursPositive() {
        assertDoesNotThrow(() -> testEnrollment.setCoveredHours(300));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos completos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testEnrollment.setInternDTO(new InternDTO());
            testEnrollment.setCourseDTO(new CourseDTO());
            testEnrollment.setProjectDTO(new ProjectDTO());
            testEnrollment.setFinalGrade(100);
            testEnrollment.setCoveredHours(480);
        });
    }
}