package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private UserDTO testUser;

    @BeforeEach
    void setUpEach() {
        testUser = new UserDTO();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("firstName: debe lanzar excepción cuando es null o vacío")
    void testFirstNameRequired(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setFirstName(invalidValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("firstLastName: debe lanzar excepción cuando es null o vacío")
    void testFirstLastNameRequired(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setFirstLastName(invalidValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("email: debe lanzar excepción cuando es null o vacío")
    void testEmailRequired(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setEmail(invalidValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("phoneNumber: debe lanzar excepción cuando es null o vacío")
    void testPhoneNumberRequired(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPhoneNumber(invalidValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("password: debe lanzar excepción cuando es null o vacío")
    void testPasswordRequired(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(invalidValue));
    }

    @Test
    @DisplayName("firstName: debe lanzar excepción cuando excede 30 caracteres")
    void testFirstNameMaxLength() {
        String tooLong = "Nicooooooooooooooooooooooooooooooole";
        assertThrows(IllegalArgumentException.class, () -> testUser.setFirstName(tooLong));
    }

    @Test
    @DisplayName("secondName: debe lanzar excepción cuando excede 30 caracteres (opcional)")
    void testSecondNameMaxLength() {
        String tooLong = "Stellaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertThrows(IllegalArgumentException.class, () -> testUser.setSecondName(tooLong));
    }

    @Test
    @DisplayName("firstLastName: debe lanzar excepción cuando excede 30 caracteres")
    void testFirstLastNameMaxLength() {
        String tooLong = "Armaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaas";
        assertThrows(IllegalArgumentException.class, () -> testUser.setFirstLastName(tooLong));
    }

    @Test
    @DisplayName("secondLastName: debe lanzar excepción cuando excede 30 caracteres (opcional)")
    void testSecondLastNameMaxLength() {
        String tooLong = "Mendozaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertThrows(IllegalArgumentException.class, () -> testUser.setSecondLastName(tooLong));
    }

    @Test
    @DisplayName("email: debe lanzar excepción cuando excede 30 caracteres")
    void testEmailMaxLength() {
        String tooLong = "nicole_stella_armas_mendoza_20061206@gmail.com";
        assertThrows(IllegalArgumentException.class, () -> testUser.setEmail(tooLong));
    }

    @Test
    @DisplayName("phoneNumber: debe lanzar excepción cuando excede 10 caracteres")
    void testPhoneNumberMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPhoneNumber("228123456789"));
    }

    @Test
    @DisplayName("password: debe lanzar excepción cuando excede 255 caracteres")
    void testPasswordMaxLength() {
        String tooLong = "A1!" + "a".repeat(253);
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(tooLong));
    }

    @ParameterizedTest
    @ValueSource(strings = {"armasstellagmail.com", "sin@dominio", "usuario@dominio"})
    @DisplayName("email: debe lanzar excepción cuando el formato es inválido")
    void testEmailInvalidFormat(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setEmail(invalidEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "12345678AB", "228-123-4567", "abc1234567"})
    @DisplayName("phoneNumber: debe lanzar excepción cuando el formato es inválido (no 10 dígitos)")
    void testPhoneNumberInvalidFormat(String invalidPhone) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPhoneNumber(invalidPhone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "PASSWORD123", "Pass123456", "Pass!@#", "PASS123!@", "pass123!@", "Password!"})
    @DisplayName("password: debe lanzar excepción cuando no cumple con una contraseña segura")
    void testPasswordInvalidFormat(String invalidPassword) {
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(invalidPassword));
    }

    @Test
    @DisplayName("Debe aceptar un usuario con datos válidos (incluyendo campos opcionales)")
    void testValidUserSuccess() {
        assertDoesNotThrow(() -> {
            testUser.setFirstName("María");
            testUser.setSecondName("Isabel");
            testUser.setFirstLastName("González");
            testUser.setSecondLastName("Pérez");
            testUser.setEmail("zs24013311@estudiantes.uv.mx");
            testUser.setPhoneNumber("2281234567");
            testUser.setPassword("Password123!");
        });
    }

    @Test
    @DisplayName("Debe aceptar campos opcionales como null")
    void testOptionalFieldsCanBeNull() {
        assertDoesNotThrow(() -> {
            testUser.setFirstName("Juan");
            testUser.setSecondName(null);
            testUser.setFirstLastName("López");
            testUser.setSecondLastName(null);
            testUser.setEmail("juan@uv.mx");
            testUser.setPhoneNumber("2287654321");
            testUser.setPassword("SecureP@ssw0rd");
        });
    }

    @Test
    @DisplayName("Debe aceptar nombre con exactamente 30 caracteres (límite)")
    void testNameAtMaxLength() {
        String thirtyCharsName = "Evangelina del Socorro Socorro";
        assertDoesNotThrow(() -> testUser.setFirstName(thirtyCharsName));
    }

    @Test
    @DisplayName("Debe aceptar email con exactamente 30 caracteres (límite)")
    void testEmailAtMaxLength() {
        String thirtyCharsEmail = "zszs24013315@estudiantes.uv.mx";
        assertDoesNotThrow(() -> testUser.setEmail(thirtyCharsEmail));
    }
}
