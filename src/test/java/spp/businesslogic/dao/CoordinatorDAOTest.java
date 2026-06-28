package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        String uniqueEmail = "coord" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@uv.mx";
        String uniquePersonalNumber = "C" + uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniquePhone = "228" + uniqueSuffix.substring(uniqueSuffix.length() - 7);

        testCoordinator.setFirstName("Ana");
        testCoordinator.setSecondName("");
        testCoordinator.setFirstLastName("Martinez");
        testCoordinator.setSecondLastName("");
        testCoordinator.setEmail(uniqueEmail);
        testCoordinator.setPhoneNumber(uniquePhone);
        testCoordinator.setPassword("Coord123!");
        testCoordinator.setPersonalNumber(uniquePersonalNumber);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un coordinador correctamente")
    void testRegisterCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.registerCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Debe lanzar DAOException al intentar registrar un coordinador duplicado")
    void testRegisterCoordinatorDuplicate() throws DAOException {
        coordinatorDAO.registerCoordinator(testCoordinator);

        DAOException exception = assertThrows(DAOException.class, () -> {
            coordinatorDAO.registerCoordinator(testCoordinator);
        });

        assertTrue(exception.getMessage().contains("El Usuario que usted está intentando registrar ya existe"));
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe verificar que un coordinador activo existe por número personal")
    void testExistsActiveCoordinatorByPersonalNumberTrue() throws DAOException {
        coordinatorDAO.registerCoordinator(testCoordinator);

        boolean exists = coordinatorDAO.existsActiveCoordinatorByPersonalNumber(testCoordinator.getPersonalNumber());
        assertTrue(exists);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe devolver false al buscar un número personal inexistente")
    void testExistsActiveCoordinatorByPersonalNumberFalse() throws DAOException {
        boolean exists = coordinatorDAO.existsActiveCoordinatorByPersonalNumber("C99999999");
        assertFalse(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Debe obtener la lista de coordinadores activos y verificar que el registrado esté presente")
    void testGetActiveCoordinators() throws DAOException {
        coordinatorDAO.registerCoordinator(testCoordinator);

        CoordinatorDTO expected = new CoordinatorDTO();
        expected.setFirstName(testCoordinator.getFirstName());
        expected.setFirstLastName(testCoordinator.getFirstLastName());
        expected.setEmail(testCoordinator.getEmail());
        expected.setPersonalNumber(testCoordinator.getPersonalNumber());

        List<CoordinatorDTO> actualList = coordinatorDAO.getActiveCoordinators();
        assertFalse(actualList.isEmpty());

        CoordinatorDTO found = actualList.stream()
                .filter(coordinator -> coordinator.getPersonalNumber().equals(expected.getPersonalNumber()))
                .findFirst()
                .orElse(null);

        assertNotNull(found);

        assertEquals(expected.getFirstName().trim(), found.getFirstName().trim());
        assertEquals(expected.getFirstLastName().trim(), found.getFirstLastName().trim());
        assertEquals(expected.getEmail(), found.getEmail());
        assertEquals(expected.getPersonalNumber(), found.getPersonalNumber());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Normal: Debe desactivar un coordinador correctamente")
    void testDeactivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.registerCoordinator(testCoordinator);

        boolean result = coordinatorDAO.deactivateCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Desactivar un coordinador que no existe debe devolver false")
    void testDeactivateCoordinatorNotFound() throws DAOException {
        CoordinatorDTO fake = new CoordinatorDTO();
        fake.setPersonalNumber("C99999999");

        boolean result = coordinatorDAO.deactivateCoordinator(fake);
        assertFalse(result);
    }
}