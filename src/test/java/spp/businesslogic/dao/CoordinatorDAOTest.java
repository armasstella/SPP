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
    }

    @BeforeEach
    void setUp() {
        testCoordinator = new CoordinatorDTO();
        testCoordinator.setStatus("null");
        testCoordinator.setLastConnection("2025-07-07 12:00:00");
        testCoordinator.setFirstName("Stella");
        testCoordinator.setSecondName("");
        testCoordinator.setFirstLastName("Armas");
        testCoordinator.setSecondLastName("Mendoza");
        testCoordinator.setEmail("armaaaaas@uv.mx");
        testCoordinator.setPhoneNumber("92490004567");
        testCoordinator.setPassword("ilmgwnil.");
        testCoordinator.setPersonalNumber("78243");
    }

    @Test
    @DisplayName("Debe insertar un coordinador exitosamente")
    void testAddCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");
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

        boolean result = coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertTrue(result);
    }


}