package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectDTOTest {

    private ProjectDTO testProject;

    @BeforeEach
    void setUpEach() {
        testProject = new ProjectDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con nombre nulo")
    void testSetNameNull() {
        assertThrows(IllegalArgumentException.class, () -> testProject.setName(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con descripción nula")
    void testSetDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () -> testProject.setDescription(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con nombre demasiado largo")
    void testSetNameTooLong() {
        String longName = "A".repeat(151);
        assertThrows(IllegalArgumentException.class, () -> testProject.setName(longName));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con descripción demasiado larga")
    void testSetDescriptionTooLong() {
        String longDescription = "A".repeat(501);
        assertThrows(IllegalArgumentException.class, () -> testProject.setDescription(longDescription));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con cupo negativo")
    void testSetPlacesAvailableNegative() {
        assertThrows(IllegalArgumentException.class, () -> testProject.setPlacesAvailable(-1));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar Encargado nulo")
    void testSetProjectManagerNull() {
        assertThrows(IllegalArgumentException.class, () -> testProject.setProjectManagerDTO(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar Organización nula")
    void testSetLinkedOrganizationNull() {
        assertThrows(IllegalArgumentException.class, () -> testProject.setLinkedOrganizationDTO(null));
    }

    @Test
    @DisplayName("Debe aceptar cupo en cero")
    void testSetPlacesAvailableZero() {
        assertDoesNotThrow(() -> testProject.setPlacesAvailable(0));
    }

    @Test
    @DisplayName("Debe aceptar disponibilidad nula (opcional)")
    void testSetAvailabilityNull() {
        assertDoesNotThrow(() -> testProject.setAvailability(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con disponibilidad demasiado larga")
    void testSetAvailabilityTooLong() {
        String longAvail = "A".repeat(21);
        assertThrows(IllegalArgumentException.class, () -> testProject.setAvailability(longAvail));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos completos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testProject.setName("Sistema Web");
            testProject.setDescription("Desarrollo de un ERP");
            testProject.setAvailability("Disponible");
            testProject.setPlacesAvailable(3);
            testProject.setProjectManagerDTO(new ProjectManagerDTO());
            testProject.setLinkedOrganizationDTO(new LinkedOrganizationDTO());
        });
    }
}