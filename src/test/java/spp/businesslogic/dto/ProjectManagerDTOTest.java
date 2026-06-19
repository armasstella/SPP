package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectManagerDTOTest {

    private ProjectManagerDTO testManager;

    @BeforeEach
    void setUpEach() {
        testManager = new ProjectManagerDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar primer nombre nulo")
    void testSetFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setFirstName(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar apellido paterno nulo")
    void testSetFirstLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setFirstLastName(null));
    }

    @Test
    @DisplayName("Debe aceptar segundo nombre y apellido materno nulos (opcionales)")
    void testSetOptionalNamesNullSuccess() {
        assertDoesNotThrow(() -> {
            testManager.setSecondName(null);
            testManager.setSecondLastName(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con nombre demasiado largo")
    void testSetFirstNameTooLong() {
        String longName = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> testManager.setFirstName(longName));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar rol nulo")
    void testSetRoleNull() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setRole(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar responsabilidad nula")
    void testSetResponsibilityNull() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setResponsibility(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con responsabilidad demasiado larga")
    void testSetResponsibilityTooLong() {
        String longResp = "A".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> testManager.setResponsibility(longResp));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con teléfono nulo")
    void testSetPhoneNull() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setPhoneNumber(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con teléfono de longitud incorrecta")
    void testSetPhoneShort() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setPhoneNumber("123456789"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con teléfono conteniendo letras")
    void testSetPhoneLetters() {
        assertThrows(IllegalArgumentException.class, () -> testManager.setPhoneNumber("12345678AB"));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos completos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testManager.setFirstName("Juan");
            testManager.setSecondName("Carlos");
            testManager.setFirstLastName("Pérez");
            testManager.setSecondLastName("López");
            testManager.setRole("Jefe de Desarrollo");
            testManager.setResponsibility("Supervisión de proyectos");
            testManager.setPhoneNumber("2281234567");
        });
    }

    @Test
    @DisplayName("Debe aceptar datos válidos mínimos (sin opcionales)")
    void testSetValidMinimalDataSuccess() {
        assertDoesNotThrow(() -> {
            testManager.setFirstName("María");
            testManager.setFirstLastName("Gómez");
            testManager.setRole("Coordinadora");
            testManager.setResponsibility("Gestión de equipo");
            testManager.setPhoneNumber("5551234567");
        });
    }
}