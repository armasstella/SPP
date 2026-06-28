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
public class LinkedOrganizationDTOTest {

    private LinkedOrganizationDTO organizationDTO;

    @BeforeEach
    void setUp() {
        organizationDTO = new LinkedOrganizationDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe aceptar un RFC con formato válido")
    void testSetRfcValidFormat() {
        organizationDTO.setRfc("GOD561231MN8");
        assertTrue(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().isEmpty());
        assertEquals("GOD561231MN8", organizationDTO.getRfc());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Debe rechazar un RFC con formato inválido (caracteres incorrectos)")
    void testSetRfcInvalidFormat() {
        organizationDTO.setRfc("GODE561231MN");
        assertFalse(organizationDTO.isValid());
        assertEquals(1, organizationDTO.getErrors().size());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
        assertNull(organizationDTO.getRfc());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Debe rechazar un RFC con formato inválido (fecha incorrecta)")
    void testSetRfcInvalidDate() {
        organizationDTO.setRfc("GODE563231MN8");
        assertFalse(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
        assertNull(organizationDTO.getRfc());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe rechazar un RFC con formato inválido (solo letras)")
    void testSetRfcInvalidOnlyLetters() {
        organizationDTO.setRfc("ABCDEFGHIJKLM");
        assertFalse(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
        assertNull(organizationDTO.getRfc());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Debe aceptar un correo electrónico con formato válido")
    void testSetEmailValidFormat() {
        organizationDTO.setEmail("contacto@org.com");
        assertTrue(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().isEmpty());
        assertEquals("contacto@org.com", organizationDTO.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe rechazar un correo sin dominio")
    void testSetEmailInvalidNoDomain() {
        organizationDTO.setEmail("contacto@");
        assertFalse(organizationDTO.isValid());
        assertEquals(1, organizationDTO.getErrors().size());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(organizationDTO.getEmail());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Debe rechazar un correo sin arroba")
    void testSetEmailInvalidNoAtSymbol() {
        organizationDTO.setEmail("contacto.org.com");
        assertFalse(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(organizationDTO.getEmail());
    }

    @Test
    @Order(8)
    @DisplayName("Flujo de Acumulación: Debe acumular errores si fallan RFC y email")
    void testAccumulateMultipleErrors() {
        organizationDTO.setRfc("RFC123");
        organizationDTO.setEmail("correo-invalido.com");

        assertFalse(organizationDTO.isValid());
        assertEquals(2, organizationDTO.getErrors().size());
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
        assertTrue(organizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(organizationDTO.getRfc());
        assertNull(organizationDTO.getEmail());
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Debe ser válido cuando RFC y email son correctos")
    void testAllValidFields() {
        organizationDTO.setRfc("GOD561231MN8");
        organizationDTO.setEmail("contacto@valido.com");

        assertTrue(organizationDTO.isValid());
        assertTrue(organizationDTO.getErrors().isEmpty());
        assertEquals("GOD561231MN8", organizationDTO.getRfc());
        assertEquals("contacto@valido.com", organizationDTO.getEmail());
    }
}