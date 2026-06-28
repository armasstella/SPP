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
public class InstructorDTOTest {

    private InstructorDTO instructorDTO;

    @BeforeEach
    void setUp() {
        instructorDTO = new InstructorDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe aceptar un correo electrónico con formato válido")
    void testSetEmailValidFormat() {
        instructorDTO.setEmail("instructor@uv.mx");
        assertTrue(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().isEmpty());
        assertEquals("instructor@uv.mx", instructorDTO.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe aceptar una contraseña con formato válido")
    void testSetPasswordValidFormat() {
        instructorDTO.setPassword("Inst123!");
        assertTrue(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().isEmpty());
        assertEquals("Inst123!", instructorDTO.getPassword());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Debe rechazar un correo sin dominio")
    void testSetEmailInvalidFormatNoDomain() {
        instructorDTO.setEmail("instructor@");
        assertFalse(instructorDTO.isValid());
        assertEquals(1, instructorDTO.getErrors().size());
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(instructorDTO.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin mayúsculas")
    void testSetPasswordInvalidNoUpperCase() {
        instructorDTO.setPassword("inst123!");
        assertFalse(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(instructorDTO.getPassword());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin caracteres especiales")
    void testSetPasswordInvalidNoSpecialChar() {
        instructorDTO.setPassword("Inst1234");
        assertFalse(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(instructorDTO.getPassword());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña menor a 8 caracteres")
    void testSetPasswordInvalidTooShort() {
        instructorDTO.setPassword("I123!");
        assertFalse(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(instructorDTO.getPassword());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo de Acumulación: Debe acumular errores si fallan email y contraseña")
    void testAccumulateMultipleErrors() {
        instructorDTO.setEmail("correo-invalido.com");
        instructorDTO.setPassword("pass");
        assertFalse(instructorDTO.isValid());
        assertEquals(2, instructorDTO.getErrors().size());
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(instructorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Debe ser válido cuando email y contraseña son correctos")
    void testAllValidFields() {
        instructorDTO.setEmail("inst@uv.mx");
        instructorDTO.setPassword("Inst123!");
        assertTrue(instructorDTO.isValid());
        assertTrue(instructorDTO.getErrors().isEmpty());
        assertEquals("inst@uv.mx", instructorDTO.getEmail());
        assertEquals("Inst123!", instructorDTO.getPassword());
    }
}