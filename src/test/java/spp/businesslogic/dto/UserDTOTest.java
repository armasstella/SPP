package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.utils.businessconstants.BusinessConstant;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe aceptar un correo electrónico con formato válido")
    void testSetEmailValidFormat() {
        boolean result = userDTO.setEmail("stella.test@uv.mx");

        assertTrue(result);
        assertTrue(userDTO.isValid());
        assertTrue(userDTO.getErrors().isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe aceptar una contraseña con formato válido (incluyendo todos los requisitos)")
    void testSetPasswordValidFormat() {
        boolean result = userDTO.setPassword("StellaTest123!");

        assertTrue(result);
        assertTrue(userDTO.isValid());
        assertTrue(userDTO.getErrors().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Debe rechazar un correo sin dominio y mostrar el error")
    void testSetEmailInvalidFormatNoDomain() {
        boolean result = userDTO.setEmail("stella.test@");

        assertFalse(result);
        assertFalse(userDTO.isValid());
        assertEquals(1, userDTO.getErrors().size());
        assertTrue(userDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe rechazar un correo con espacios intermedios y registrar el error")
    void testSetEmailInvalidFormatWithSpaces() {
        boolean result = userDTO.setEmail("stella @uv.mx");

        assertFalse(result);
        assertFalse(userDTO.isValid());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin mayúsculas")
    void testSetPasswordInvalidNoUpperCase() {
        boolean result = userDTO.setPassword("stellatest123!");

        assertFalse(result);
        assertFalse(userDTO.isValid());
        assertTrue(userDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña sin caracteres especiales")
    void testSetPasswordInvalidNoSpecialChar() {
        boolean result = userDTO.setPassword("StellaTest1234");

        assertFalse(result);
        assertFalse(userDTO.isValid());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Debe rechazar una contraseña menor a 8 caracteres")
    void testSetPasswordInvalidTooShort() {
        boolean result = userDTO.setPassword("Stel1!");

        assertFalse(result);
        assertFalse(userDTO.isValid());
    }

    @Test
    @Order(8)
    @DisplayName("Flujo de Acumulación: Debe acumular múltiples errores si fallan varios campos")
    void testAccumulateMultipleErrors() {
        userDTO.setEmail("correo-invalido.com");
        userDTO.setPassword("pass");

        assertFalse(userDTO.isValid());
        assertEquals(2, userDTO.getErrors().size());
        assertTrue(userDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(userDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
    }
}