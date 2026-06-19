package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseDTOTest {

    private CourseDTO testCourse;

    @BeforeEach
    void setUpEach() {
        testCourse = new CourseDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con NRC cero o negativo")
    void testSetCourseCodeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setCourseCode(0));
        assertThrows(IllegalArgumentException.class, () -> testCourse.setCourseCode(-5));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con periodo nulo o vacío")
    void testSetTermNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setTerm(null));
        assertThrows(IllegalArgumentException.class, () -> testCourse.setTerm("   "));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con periodo muy largo")
    void testSetTermTooLong() {
        String longTerm = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> testCourse.setTerm(longTerm));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con bloque cero o negativo")
    void testSetSchoolBlockInvalid() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setSchoolBlock(0));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con sección cero o negativa")
    void testSetSectionInvalid() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setSection(-1));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con cupo cero o negativo")
    void testSetCapacityInvalid() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setCapacity(0));
    }

    @Test
    @DisplayName("Debe aceptar detalles del curso nulos (campo opcional)")
    void testSetCourseDetailsNull() {
        assertDoesNotThrow(() -> testCourse.setCourseDetails(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con detalles muy largos")
    void testSetCourseDetailsTooLong() {
        String longDetails = "A".repeat(501);
        assertThrows(IllegalArgumentException.class, () -> testCourse.setCourseDetails(longDetails));
    }

    @Test
    @DisplayName("Debe aceptar profesor nulo (curso sin asignar)")
    void testSetInstructorNull() {
        assertDoesNotThrow(() -> testCourse.setInstructorDTO(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con cantidad de practicantes negativa")
    void testSetNumberOfInternsNegative() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.setNumberOfInterns(-5));
    }

    @Test
    @DisplayName("Debe aceptar cantidad de practicantes igual a cero")
    void testSetNumberOfInternsZero() {
        assertDoesNotThrow(() -> testCourse.setNumberOfInterns(0));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testCourse.setCourseCode(85421);
            testCourse.setTerm("FEBRERO-JULIO 2026");
            testCourse.setSchoolBlock(1);
            testCourse.setSection(2);
            testCourse.setCapacity(30);
            testCourse.setCourseDetails("Laboratorio de cómputo 4");
            testCourse.setNumberOfInterns(15);
        });
    }
}