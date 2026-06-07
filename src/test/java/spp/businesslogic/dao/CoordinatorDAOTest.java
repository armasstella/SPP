package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

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
        testCoordinator.setStatus("Activo");
        testCoordinator.setLastConnection("2025-05-12 12:00:00");
        testCoordinator.setFirstName("Leonardo");
        testCoordinator.setSecondName(null);
        testCoordinator.setFirstLastName("Masin");
        testCoordinator.setSecondLastName(null);
        testCoordinator.setEmail("leomado@uv.mx");
        testCoordinator.setPhoneNumber("8565567890");
        testCoordinator.setPassword("le000ps");
        testCoordinator.setPersonalNumber("14101");
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
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean result = coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe activar un coordinador existente")
    void testActivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean result = coordinatorDAO.activateCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar error al insertar coordinador sin número de personal")
    void testAddCoordinatorFailedNullPersonalNumber() throws DAOException {
        testCoordinator.setPersonalNumber(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar coordinador sin contraseña")
    void testAddCoordinatorFailedNullPassword() throws DAOException {
        testCoordinator.setPassword(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al agregar coordinador con correo nulo")
    void testAddCoordinatorFailedNullEmail() throws DAOException {
        testCoordinator.setEmail(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al agregar coordinador con teléfono nulo")
    void testAddCoordinatorFailedNullPhoneNumber() throws DAOException {
        testCoordinator.setPhoneNumber(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al agregar coordinador con apellido paterno nulo")
    void testAddCoordinatorFailedNullFirstLastName() throws DAOException {
        testCoordinator.setFirstLastName(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar coordinador con nombre nulo")
    void testAddCoordinatorFailedNullFirstName() throws DAOException {
        testCoordinator.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al intentar inactivar dos veces el mismo coordinador")
    void testInactivateCoordinatorFailedAlreadyInactive() throws DAOException {
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertThrows(DAOException.class, () -> {
            coordinatorDAO.inactivateCoordinator(testCoordinator);
        });
    }
}