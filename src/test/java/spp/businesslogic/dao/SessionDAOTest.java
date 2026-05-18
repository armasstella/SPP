package spp.businesslogic.dao;

import org.junit.jupiter.api.*;
import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.SessionDAO;
import spp.dataaccess.dao.UserDAO;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionDAOTest {

    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private SessionDTO testSession;

    @BeforeAll
    void setupAll() {
        sessionDAO = new SessionDAO();
        userDAO = new UserDAO();
    }

    @BeforeEach
    void setUp() {
        String email = "zS24013314@estudiantes.uv.mx";
    }

    @Test
    @DisplayName("Debe generar una sesión correctamente")
    void testCreateSessionSuccess() {
        String email = "zS24013314@estudiantes.uv.mx";
        boolean result = false;
        try {
            sessionDAO.createSession(userDAO.obtainId(email));
            result = true;
        } catch (DAOException e) {
            System.out.println("Error");
        }
        assertTrue(result, "El método debería retornar true al crear la conexión");
    }

    @Test
    @DisplayName("Debe buscar el token y devolver el DTO con el correo del usuario")
    void testSearchSessionSuccess() {

    }


}
