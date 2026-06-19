package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkedOrganizationDTOTest {

    private LinkedOrganizationDTO testOrganization;

    @BeforeEach
    void setUpEach() {
        testOrganization = new LinkedOrganizationDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar nombre nulo")
    void testSetNameNull() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setName(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar RFC nulo")
    void testSetRfcNull() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setRfc(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar dirección nula")
    void testSetAddressNull() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setAddress(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar ciudad nula")
    void testSetCityNull() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setCity(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con RFC en minúsculas")
    void testSetRfcInvalidLowercase() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setRfc("abc123456xyz"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con RFC demasiado corto")
    void testSetRfcInvalidShort() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setRfc("ABC123"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con teléfono con letras")
    void testSetPhoneInvalidLetters() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setPhoneNumber("12345678AB"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con teléfono de 9 dígitos")
    void testSetPhoneInvalidShort() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setPhoneNumber("123456789"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con email sin arroba")
    void testSetEmailInvalidMissingAt() {
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setEmail("correosinarroba"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con nombre muy largo (>50)")
    void testSetNameTooLong() {
        String longName = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setName(longName));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con RFC muy largo (>15)")
    void testSetRfcTooLong() {
        String longRfc = "A".repeat(16);
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setRfc(longRfc));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con dirección muy larga (>50)")
    void testSetAddressTooLong() {
        String longAddress = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setAddress(longAddress));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con giro muy largo (>30)")
    void testSetBusinessTooLong() {
        String longBusiness = "A".repeat(31);
        assertThrows(IllegalArgumentException.class, ()
                -> testOrganization.setBusiness(longBusiness));
    }

    @Test
    @DisplayName("Debe aceptar datos válidos y no lanzar excepciones")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testOrganization.setName("Desarrollo XYZ");
            testOrganization.setRfc("XYZ123456ABC");
            testOrganization.setPhoneNumber("2281234567");
            testOrganization.setEmail("contacto@xyz.com");
        });
    }
}