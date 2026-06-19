package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDTOTest {

    private UserDTO testUser;

    @BeforeEach
    void setUpEach() {
        testUser = new UserDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar nombre nulo")
    void testSetFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setFirstName(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar email inválido (sin arroba)")
    void testSetEmailFailedInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setEmail("correosinarroba"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar teléfono de menos de 10 dígitos")
    void testSetPhoneNumberFailedShort() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPhoneNumber("123456789"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar teléfono con letras")
    void testSetPhoneNumberFailedLetters() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPhoneNumber("12345678AB"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar contraseña débil (sin mayúscula)")
    void testSetPasswordFailedNoUppercase() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword("pass123!@"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar contraseña sin carácter especial")
    void testSetPasswordFailedNoSpecial() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword("Pass123456"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar contraseña demasiado corta")
    void testSetPasswordFailedTooShort() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword("Pa1!"));
    }

    @Test
    @DisplayName("Debe aceptar un usuario con datos válidos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testUser.setFirstName("María");
            testUser.setFirstLastName("González");
            testUser.setEmail("zs24013315@estudiantes.uv.mx");
            testUser.setPhoneNumber("2281234567");
            testUser.setPassword("Password123!");
        });
    }
}