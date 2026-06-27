package spp.businesslogic.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private UserDAO userDAO;
    private UserDTO testUser;

    @BeforeAll
    void setupAll() {
        userDAO = new UserDAO();
        testUser = new UserDTO();
    }

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "admin.test@uv.mx";

                //"stella" + uniqueSuffix.substring(
                //uniqueSuffix.length() - 8) + "@uv.mx";

        testUser.setFirstName("Juan");
        testUser.setSecondName("Ernesto");
        testUser.setFirstLastName("Sanchez");
        testUser.setSecondLastName("Pérez");
        testUser.setEmail(uniqueEmail);
        testUser.setPhoneNumber("2284457188");
        testUser.setPassword("AdminTest123!");
    }

    @Test
    @DisplayName("Debe insertar un administrador y devolver un id generado mayor a 0")
    void testAddAdminSuccess() throws DAOException {
        int generatedId = userDAO.registerUser(testUser);
        Assertions.assertTrue(generatedId > 0);
    }

    @Test
    @DisplayName("Debe obtener el id del administrador recién insertado")
    void testObtainIdSuccess() throws DAOException {
        userDAO.registerUser(testUser);
        int result = userDAO.obtainId(testUser.getEmail());
        Assertions.assertTrue(result > 0);
    }

    @Test
    @DisplayName("Debe autenticar correctamente al administrador")
    void testLoginSuccess() throws DAOException {
        userDAO.registerUser(testUser);
        LoginResultDTO result = userDAO.login(testUser.getEmail(), testUser.getPassword());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Administrador", result.getUserType());
    }

    @Test
    @DisplayName("Debe lanzar DAOException con contraseña incorrecta")
    void testLoginWrongPassword() throws DAOException {
        userDAO.registerUser(testUser);
        assertThrows(DAOException.class, () -> {
            userDAO.login(testUser.getEmail(), "ContraseñaWrong123!");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al insertar un email duplicado")
    void testAddAdminDuplicateEmail() throws  DAOException {
        userDAO.registerUser(testUser);
        assertThrows(DAOException.class, () ->
                userDAO.registerUser(testUser));
    }

    @Test
    @DisplayName("Debe devolver null al iniciar sesión con correo inexistente")
    void testLoginNonExistentEmail() throws DAOException {
        LoginResultDTO result = userDAO.login("zs21123423@estudiantes.uv.mx",
                "qAmNAO91A.A");
        assertNull(result);
    }

    @Test
    @DisplayName("Debe lanzar error al insertar usuario sin correo")
    void testRegisterUserNullEmail() {
        testUser.setEmail(null);
        assertThrows(DAOException.class, () -> {
            userDAO.registerUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar usuario sin contraseña")
    void testRegisterUserNullPassword() {
        testUser.setPassword(null);
        assertThrows(DAOException.class, () -> {
            userDAO.registerUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al obtener id de un correo inexistente")
    void testObtainIdNonExistentEmail() {
        assertThrows(DAOException.class, () -> {
            userDAO.obtainId("inexistente121@uv.mx");
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar usuario sin nombre")
    void testRegisterUserNullFirstName() {
        testUser.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            userDAO.registerUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar usuario sin apellido")
    void testRegisterUserNullFirstLastName() {
        testUser.setFirstLastName(null);
        assertThrows(DAOException.class, () -> {
            userDAO.registerUser(testUser);
        });
    }

    @Test
    @DisplayName("Debe insertar un usuario con nombre")
    void testRegisterUserWithFirstNameSuccess() throws DAOException {
        testUser.setFirstName("Ana");
        testUser.setEmail("zs24012212@estudiantes.uv.mx");
        int generatedId = userDAO.registerUser(testUser);
        assertTrue(generatedId > 0);
    }
}