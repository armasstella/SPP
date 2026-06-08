package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionDAOTest {

    private SessionDAO sessionDAO;
    private UserDAO userDAO;

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
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe buscar el token y devolver el DTO con el correo del usuario")
    void testSearchSessionSuccess() throws DAOException {
        String email = "zS24013314@estudiantes.uv.mx";
        String token = sessionDAO.createSession(userDAO.obtainId(email));
        SessionDTO session = sessionDAO.searchSession(token);
        assertNotNull(session);
        assertEquals(email, session.getEmail());
    }

    @Test
    @DisplayName("Debe devolver null cuando el token no existe")
    void testSearchSessionFailedInvalidToken() throws DAOException {
        SessionDTO session = sessionDAO.searchSession("TOKEN INEXISTENTE");
        assertNull(session);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar un token nulo")
    void testSearchSessionFailedNullToken() {
        assertThrows(DAOException.class, () -> {
            sessionDAO.searchSession(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al crear sesión para usuario inexistente")
    void testCreateSessionFailedInvalidUser() {
        assertThrows(DAOException.class, () -> {
            sessionDAO.createSession(999999);
        });
    }

    @Test
    @DisplayName("Debe generar tokens distintos para cada sesión")
    void testCreateSessionGeneratesDifferentTokens() throws DAOException {
        int userId = userDAO.obtainId("zS24013314@estudiantes.uv.mx");
        String token1 = sessionDAO.createSession(userId);
        String token2 = sessionDAO.createSession(userId);
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Debe generar sesiones para el mismo usuario")
    void testCreateMultipleSessionsSuccess() throws DAOException {
        int userId = userDAO.obtainId("zS24013314@estudiantes.uv.mx");
        String token1 = sessionDAO.createSession(userId);
        String token2 = sessionDAO.createSession(userId);
        assertNotNull(token1);
        assertNotNull(token2);
    }

    @Test
    @DisplayName("Debe generar un token válido")
    void testCreateSessionReturnsToken() throws DAOException {
        String token = sessionDAO.createSession(
                userDAO.obtainId("zS24013314@estudiantes.uv.mx"));
        assertNotNull(token);
    }

    @Test
    @DisplayName("Debe permitir eliminar una sesión inexistente sin lanzar excepción")
    void testDeleteSessionNonExistentToken() throws DAOException {
        assertDoesNotThrow(() -> {
            sessionDAO.deleteSession("TOKEN INEXISTENTE");
        });
    }
}
