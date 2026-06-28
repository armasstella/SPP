package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.utils.businessconstants.BusinessConstant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoordinatorDTOTest {

    private CoordinatorDTO coordinatorDTO;

    @BeforeEach
    void setUp() {
        coordinatorDTO = new CoordinatorDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe aceptar un correo electrónico con formato válido")
    void testSetEmailValidFormat() {
        coordinatorDTO.setEmail("coordinador@uv.mx");
        assertTrue(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().isEmpty());
        assertEquals("coordinador@uv.mx", coordinatorDTO.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe aceptar una contraseña con formato válido")
    void testSetPasswordValidFormat() {
        coordinatorDTO.setPassword("Coord123!");
        assertTrue(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().isEmpty());
        assertEquals("Coord123!", coordinatorDTO.getPassword());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Debe rechazar un correo sin dominio")
    void testSetEmailInvalidFormatNoDomain() {
        coordinatorDTO.setEmail("coordinador@");
        assertFalse(coordinatorDTO.isValid());
        assertEquals(1, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(coordinatorDTO.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin mayúsculas")
    void testSetPasswordInvalidNoUpperCase() {
        coordinatorDTO.setPassword("coord123!");
        assertFalse(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(coordinatorDTO.getPassword());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin caracteres especiales")
    void testSetPasswordInvalidNoSpecialChar() {
        coordinatorDTO.setPassword("Coord1234");
        assertFalse(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(coordinatorDTO.getPassword());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña menor a 8 caracteres")
    void testSetPasswordInvalidTooShort() {
        coordinatorDTO.setPassword("C123!");
        assertFalse(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(coordinatorDTO.getPassword());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo de Acumulación: Debe acumular errores si fallan email y contraseña")
    void testAccumulateMultipleErrors() {
        coordinatorDTO.setEmail("correo-invalido.com");
        coordinatorDTO.setPassword("pass");
        assertFalse(coordinatorDTO.isValid());
        assertEquals(2, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Debe ser válido cuando email y contraseña son correctos")
    void testAllValidFields() {
        coordinatorDTO.setEmail("coord@uv.mx");
        coordinatorDTO.setPassword("Coord123!");
        assertTrue(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().isEmpty());
        assertEquals("coord@uv.mx", coordinatorDTO.getEmail());
        assertEquals("Coord123!", coordinatorDTO.getPassword());
    }
}