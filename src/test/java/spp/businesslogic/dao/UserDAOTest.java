package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        String uniqueEmail = "stella" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@uv.mx";
        String uniquePhone = "228" + uniqueSuffix.substring(uniqueSuffix.length() - 7);

        testUser.setFirstName("Nicole");
        testUser.setSecondName("");
        testUser.setFirstLastName("Armas");
        testUser.setSecondLastName("Mendoza");
        testUser.setEmail(uniqueEmail);
        testUser.setPhoneNumber(uniquePhone);
        testUser.setPassword("StellaTest123!");
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un usuario correctamente y devolver un ID generado válido")
    void testRegisterUserSuccess() throws DAOException {
        int generatedId = userDAO.registerUser(testUser);
        assertTrue(generatedId > 0);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe obtener el ID de un usuario existente")
    void testObtainIdSuccess() throws DAOException {
        userDAO.registerUser(testUser);
        int resultId = userDAO.obtainId(testUser.getEmail());
        assertTrue(resultId > 0);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe autenticar correctamente y retornar el DTO con éxito")
    void testLoginSuccess() throws DAOException {
        userDAO.registerUser(testUser);
        LoginResultDTO result = userDAO.login(testUser.getEmail(), testUser.getPassword());

        assertNotNull(result);
        assertNotNull(result.getUserType());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe confirmar que un correo existe devolviendo true")
    void testExistsEmailRegisterTrue() throws DAOException {
        userDAO.registerUser(testUser);
        boolean exists = userDAO.existsEmailRegister(testUser.getEmail());
        assertTrue(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe retornar false al buscar un correo que no existe")
    void testExistsEmailRegisterFalse() throws DAOException {
        boolean exists = userDAO.existsEmailRegister("fantasma_" + System.currentTimeMillis() + "@uv.mx");
        assertFalse(exists);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe retornar DTO de fallo (sin lanzar excepción) con contraseña incorrecta")
    void testLoginWrongPassword() throws DAOException {
        userDAO.registerUser(testUser);
        LoginResultDTO result = userDAO.login(testUser.getEmail(), "ClaveEquivocada123!");

        assertNull(result.getUserType());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Debe retornar DTO de fallo al iniciar sesión con correo inexistente")
    void testLoginNonExistentEmail() throws DAOException {
        LoginResultDTO result = userDAO.login("nadie_" + System.currentTimeMillis() + "@uv.mx", "AdminTest123!");

        assertNotNull(result);
        assertNull(result.getUserType());
    }

    @Test
    @Order(8)
    @DisplayName("Excepción: Debe lanzar DAOException al insertar un email duplicado")
    void testRegisterUserDuplicateData() throws DAOException {
        userDAO.registerUser(testUser);

        DAOException exception = assertThrows(DAOException.class, () -> {
            userDAO.registerUser(testUser);
        });

        assertTrue(exception.getMessage().contains("WARN: Violación de integridad de datos al insertar"));
    }

    @Test
    @Order(9)
    @DisplayName("Excepción: Debe lanzar DAOException al obtener ID de un correo inexistente")
    void testObtainIdEmailNotFound() {
        DAOException exception = assertThrows(DAOException.class, () -> {
            userDAO.obtainId("desconocido_" + System.currentTimeMillis() + "@uv.mx");
        });

        assertTrue(exception.getMessage().contains("ERROR: Usuario no encontrado con email: "));
    }
}