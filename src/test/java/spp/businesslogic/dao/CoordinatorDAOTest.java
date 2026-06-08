package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoordinatorDAOTest {

    private CoordinatorDAO coordinatorDAO;
    private CoordinatorDTO testCoordinator;

    @BeforeAll
    void setupAll() {
        coordinatorDAO = new CoordinatorDAO();
        testCoordinator = new CoordinatorDTO();
    }

    @BeforeEach
    void setUp() {

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePersonalNumber = uniqueSuffix.substring(uniqueSuffix.length() - 5);

        testCoordinator.setStatus("Activo");
        testCoordinator.setLastConnection("2025-05-12 12:00:00");
        testCoordinator.setFirstName("Leonardo");
        testCoordinator.setSecondName(null);
        testCoordinator.setFirstLastName("Masin");
        testCoordinator.setSecondLastName(null);
        testCoordinator.setEmail("leo" + uniquePersonalNumber + "@uv.mx");
        testCoordinator.setPhoneNumber("8565" + uniquePersonalNumber);
        testCoordinator.setPassword("le000ps");
        testCoordinator.setPersonalNumber(uniquePersonalNumber);
    }

    @Test
    @DisplayName("Debe insertar un coordinador exitosamente")
    void testAddCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un número de personal duplicado")
    void testAddCoordinatorFailedDuplicatePersonalNumber() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe inactivar un coordinador existente")
    void testInactivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        boolean result = coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe activar un coordinador existente")
    void testActivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean result = coordinatorDAO.activateCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar coordinador sin número de personal")
    void testAddCoordinatorFailedNullPersonalNumber() throws DAOException {
        testCoordinator.setPersonalNumber(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar coordinador sin contraseña")
    void testAddCoordinatorFailedNullPassword() throws DAOException {
        testCoordinator.setPassword(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al agregar coordinador con correo nulo")
    void testAddCoordinatorFailedNullEmail() throws DAOException {
        testCoordinator.setEmail(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al agregar coordinador con teléfono nulo")
    void testAddCoordinatorFailedNullPhoneNumber() throws DAOException {
        testCoordinator.setPhoneNumber(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al agregar coordinador con apellido paterno nulo")
    void testAddCoordinatorFailedNullFirstLastName() throws DAOException {
        testCoordinator.setFirstLastName(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar coordinador con nombre nulo")
    void testAddCoordinatorFailedNullFirstName() throws DAOException {
        testCoordinator.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe devolver true al verificar existencia de un coordinador activo")
    void testExistCoordinatorSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe devolver false al verificar existencia de un número de personal falso (Flujo Alterno)")
    void testExistCoordinatorFailedNonExistent() throws DAOException {
        boolean exists = coordinatorDAO.existCoordinator("00000");
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debe devolver false si el coordinador existe, pero está Inactivo")
    void testExistCoordinatorFailedWhenInactive() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debe recuperar correctamente la lista de todos los coordinadores activos")
    void testObtainAllActiveCoordinatorsSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        List<CoordinatorDTO> coordinators = coordinatorDAO.obtainAllActiveCoordinators();
        assertNotNull(coordinators);
        assertFalse(coordinators.isEmpty(), "La lista de coordinadores no debería estar vacía.");

        CoordinatorDTO firstCoordinator = coordinators.get(0);
        assertNotNull(firstCoordinator.getFirstName());
        assertNotNull(firstCoordinator.getEmail());
    }
}
