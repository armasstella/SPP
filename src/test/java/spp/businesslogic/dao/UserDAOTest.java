package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private UserDAO userDAO;
    private UserDTO testUser;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        userDAO = new UserDAO();
        testUser = new UserDTO();
    }

    @BeforeEach
    void setUpEach() {
        uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "z" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@uv.mx";
        String uniquePhone = "22" + uniqueSuffix.substring(uniqueSuffix.length() - 8);

        testUser.setStatus("Activo");
        testUser.setLastConnection("2025-11-22 19:15:13");
        testUser.setFirstName("Usuario");
        testUser.setSecondName("Test");
        testUser.setFirstLastName("ApellidoP");
        testUser.setSecondLastName("ApellidoM");
        testUser.setEmail(uniqueEmail);
        testUser.setPhoneNumber(uniquePhone);
        testUser.setPassword("Pass123!");
    }

    @Test
    @DisplayName("Debe insertar un usuario exitosamente")
    void testAddUserSuccess() throws DAOException {
        int id = userDAO.addUser(testUser);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe insertar usuario con segundo nombre nulo (opcional)")
    void testAddUserWithNullSecondName() throws DAOException {
        testUser.setSecondName(null);
        int id = userDAO.addUser(testUser);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe insertar usuario con segundo apellido vacío")
    void testAddUserWithEmptySecondLastName() throws DAOException {
        testUser.setSecondLastName("");
        int id = userDAO.addUser(testUser);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar usuario con correo duplicado")
    void testAddUserDuplicateEmail() throws DAOException {
        userDAO.addUser(testUser);
        UserDTO duplicate = new UserDTO();
        duplicate.setEmail(testUser.getEmail());
        duplicate.setFirstName("María");
        duplicate.setFirstLastName("Rodríguez");
        duplicate.setPhoneNumber("9241567890");
        duplicate.setPassword("Pass123!");
        assertThrows(DAOException.class, () -> userDAO.addUser(duplicate));
    }

    @Test
    @DisplayName("Debe obtener el ID del usuario por correo")
    void testObtainIdSuccess() throws DAOException {
        userDAO.addUser(testUser);
        int id = userDAO.obtainId(testUser.getEmail());
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar correo inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> userDAO.obtainId("noexiste@uv.mx"));
    }

    @Test
    @DisplayName("Debe devolver true si el correo existe")
    void testSearchEmailRegisterExists() throws DAOException {
        userDAO.addUser(testUser);
        boolean exists = userDAO.searchEmailRegister(testUser.getEmail());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe lanzar DAOException si el correo no existe")
    void testSearchEmailRegisterNotFound() {
        assertThrows(DAOException.class, () -> userDAO.searchEmailRegister("noexiste@uv.mx"));
    }

    @Test
    @DisplayName("Debe iniciar sesión exitosamente con credenciales correctas")
    void testLoginSuccess() throws DAOException {
        userDAO.addUser(testUser);
        var result = userDAO.login(testUser.getEmail(), "Pass123!");
        assertNotNull(result);
        assertEquals("Administrador", result.getUserType());
    }

    @Test
    @DisplayName("Debe lanzar DAOException con contraseña incorrecta")
    void testLoginInvalidPassword() throws DAOException {
        userDAO.addUser(testUser);
        assertThrows(DAOException.class, () -> userDAO.login(testUser.getEmail(), "wrong"));
    }

    @Test
    @DisplayName("Debe lanzar DAOException con correo inexistente")
    void testLoginEmailNotFound() {
        assertThrows(DAOException.class, () -> userDAO.login("noexiste@uv.mx", "Pass123!"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar usuario con email inválido (sin @)")
    void testAddUserInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            testUser.setEmail("correo-sin-arroba");
            userDAO.addUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar usuario con teléfono de menos de 10 dígitos")
    void testAddUserInvalidPhoneShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            testUser.setPhoneNumber("123");
            userDAO.addUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar usuario con contraseña débil (sin mayúscula)")
    void testAddUserWeakPasswordNoUppercase() {
        assertThrows(IllegalArgumentException.class, () -> {
            testUser.setPassword("pass123!");
            userDAO.addUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar usuario con contraseña sin carácter especial")
    void testAddUserWeakPasswordNoSpecial() {
        assertThrows(IllegalArgumentException.class, () -> {
            testUser.setPassword("Pass1234");
            userDAO.addUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe insertar usuario con nombres de 30 caracteres (segundo nombre vacío)")
    void testAddUserMaxLengthNames() throws DAOException {
        String longName = "A".repeat(30);
        testUser.setSecondName("");
        testUser.setSecondLastName("");
        testUser.setFirstName(longName);
        testUser.setFirstLastName(longName);
        testUser.setEmail("max" + uniqueSuffix + "@uv.mx");
        testUser.setPhoneNumber("1234567890");
        int id = userDAO.addUser(testUser);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al exceder 30 caracteres en nombre (por segundo nombre)")
    void testAddUserExceedMaxLengthName() {
        String longName = "A".repeat(30);
        testUser.setFirstName(longName);
        testUser.setSecondName("B");
        assertThrows(DAOException.class, () -> userDAO.addUser(testUser));
    }

    @Test
    @DisplayName("Debe lanzar DAOException al exceder 30 caracteres en apellidos (por segundo apellido)")
    void testAddUserExceedMaxLengthLastName() {
        String longName = "A".repeat(30);
        testUser.setFirstLastName(longName);
        testUser.setSecondLastName("B");
        assertThrows(DAOException.class, () -> userDAO.addUser(testUser));
    }
}