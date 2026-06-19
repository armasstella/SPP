package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CoordinatorDTOTest {

    private CoordinatorDTO testCoordinator;

    @BeforeEach
    void setUpEach() {
        testCoordinator = new CoordinatorDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar número personal inválido (minúsculas)")
    void testSetPersonalNumberFailedLowercase() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPersonalNumber("abc12"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar número personal demasiado corto")
    void testSetPersonalNumberFailedTooShort() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPersonalNumber("ABCD"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar número personal demasiado largo")
    void testSetPersonalNumberFailedTooLong() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPersonalNumber("ABCDEF"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar número personal nulo")
    void testSetPersonalNumberFailedNull() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPersonalNumber(null));
    }

    @Test
    @DisplayName("Debe aceptar número personal con longitud máxima de 5 caracteres")
    void testSetPersonalNumberSuccessMaxLength() {
        assertDoesNotThrow(() -> testCoordinator.setPersonalNumber("ABCDE"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar email con formato inválido")
    void testSetEmailFailedInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setEmail("correosinarroba"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar teléfono de menos de 10 dígitos")
    void testSetPhoneNumberFailedTooShort() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPhoneNumber("123"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar contraseña débil")
    void testSetPasswordFailedWeak() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPassword("pass"));
    }
}