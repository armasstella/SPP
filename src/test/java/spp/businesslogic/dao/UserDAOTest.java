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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private UserDAO userDAO;
    private UserDTO testUser;
    private int generatedId;

    @BeforeAll
    void setupAll() {
        userDAO = new UserDAO();
    }

    @BeforeEach
    void setUp() {
        testUser = new UserDTO();
        testUser.setFirstName("Admin");
        testUser.setSecondName("");
        testUser.setFirstLastName("Test");
        testUser.setSecondLastName("");
        testUser.setEmail("admin.test@uv.mx");
        testUser.setPhoneNumber("2281000000");
        testUser.setPassword("AdminTest123!");
    }

    @Test
    @Order(1)
    @DisplayName("Debe insertar un administrador y devolver un id generado mayor a 0")
    void testAddAdminSuccess() throws DAOException {
        generatedId = userDAO.addUser(testUser);
        Assertions.assertTrue(generatedId > 0,
                "El id generado debe ser mayor a 0");
    }

    @Test
    @Order(2)
    @DisplayName("Debe obtener el id del administrador recién insertado")
    void testObtainIdSuccess() throws DAOException {
        int result = userDAO.obtainId(testUser.getEmail());
        Assertions.assertTrue(result > 0,
                "No se obtuvo un id válido para el email dado");
    }

    @Test
    @Order(3)
    @DisplayName("Debe autenticar correctamente al administrador")
    void testLoginSuccess() throws DAOException {
        LoginResultDTO result = userDAO.login(testUser.getEmail(), testUser.getPassword());
        Assertions.assertNotNull(result, "El login no debe devolver null");
        Assertions.assertEquals("Administrador", result.getUserType(),
                "El tipo de usuario debe ser Administrador");
    }

    @Test
    @Order(4)
    @DisplayName("Debe fallar el login con contraseña incorrecta")
    void testLoginWrongPassword() throws DAOException {
        LoginResultDTO result = userDAO.login(testUser.getEmail(), "ContraseñaWrong123!");
        Assertions.assertNull(result, "El login debe devolver null con contraseña incorrecta");
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al insertar un email duplicado")
    void testAddAdminDuplicateEmail() {
        Assertions.assertThrows(DAOException.class, () -> userDAO.addUser(testUser),
                "Debe lanzar DAOException por email duplicado");
    }
}