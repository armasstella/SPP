package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserDAOTest {

    private UserDAO userDAO;
    private UserDTO testUser;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        userDAO = new UserDAO();
        testUser = new UserDTO();

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
    @Order(1)
    @DisplayName("Debe lanzar DAOException al insertar un usuario nulo")
    void testAddUserNullDTO() {
        assertThrows(DAOException.class, () -> userDAO.addUser(null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException al buscar correo inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> userDAO.obtainId("noexiste@uv.mx"));
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException si el correo no existe al verificar registro")
    void testSearchEmailRegisterNotFound() {
        assertThrows(DAOException.class, () -> userDAO.searchEmailRegister("noexiste@uv.mx"));
    }

    @Test
    @Order(4)
    @DisplayName("Debe lanzar DAOException al intentar login con correo inexistente")
    void testLoginEmailNotFound() {
        assertThrows(DAOException.class, () -> userDAO.login("noexiste@uv.mx", "Pass123!"));
    }

    @Test
    @Order(5)
    @DisplayName("Debe insertar un usuario exitosamente")
    void testAddUserSuccess() throws DAOException {
        int id = userDAO.addUser(testUser);
        assertTrue(id > 0);
    }

    @Test
    @Order(6)
    @DisplayName("Debe lanzar DAOException al insertar usuario con correo duplicado")
    void testAddUserDuplicateEmail() throws DAOException {
        UserDTO duplicate = new UserDTO();
        duplicate.setEmail(testUser.getEmail());
        duplicate.setFirstName("María");
        duplicate.setFirstLastName("Rodríguez");
        duplicate.setPhoneNumber("9241567890");
        duplicate.setPassword("Pass123!");
        assertThrows(DAOException.class, () -> userDAO.addUser(duplicate));
    }

    @Test
    @Order(7)
    @DisplayName("Debe obtener el ID del usuario por correo")
    void testObtainIdSuccess() throws DAOException {
        int id = userDAO.obtainId(testUser.getEmail());
        assertTrue(id > 0);
    }

    @Test
    @Order(8)
    @DisplayName("Debe devolver true si el correo existe")
    void testSearchEmailRegisterExists() throws DAOException {
        boolean exists = userDAO.searchEmailRegister(testUser.getEmail());
        assertTrue(exists);
    }

    @Test
    @Order(9)
    @DisplayName("Debe iniciar sesión exitosamente con credenciales correctas")
    void testLoginSuccess() throws DAOException {
        var result = userDAO.login(testUser.getEmail(), "Pass123!");
        assertNotNull(result);
    }

    @Test
    @Order(10)
    @DisplayName("Debe lanzar DAOException con contraseña incorrecta")
    void testLoginInvalidPassword() {
        assertThrows(DAOException.class, () -> userDAO.login(testUser.getEmail(), "wrong"));
    }

    @Test
    @Order(11)
    @DisplayName("Debe lanzar DAOException al exceder límite de BD en nombre (por segundo nombre)")
    void testAddUserExceedMaxLengthName() {
        String longName = "A".repeat(30);
        testUser.setFirstName(longName);
        testUser.setSecondName("B");
        testUser.setEmail("error1" + uniqueSuffix + "@uv.mx");
        assertThrows(DAOException.class, () -> userDAO.addUser(testUser));
    }
}