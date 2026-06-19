package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class CoordinatorDAOTest {

    private CoordinatorDAO coordinatorDAO;
    private CoordinatorDTO testCoordinator;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        coordinatorDAO = new CoordinatorDAO();
    }

    @BeforeEach
    void setUpEach() {
        testCoordinator = new CoordinatorDTO();
        uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePersonalNumber = "C" + uniqueSuffix.substring(uniqueSuffix.length() - 4);
        String uniqueEmail = "ana" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@gmail.com";
        String uniquePhone = "22" + uniqueSuffix.substring(uniqueSuffix.length() - 8);

        testCoordinator.setStatus("Activo");
        testCoordinator.setLastConnection("2025-11-22 19:15:13");
        testCoordinator.setFirstName("Ana");
        testCoordinator.setSecondName("María");
        testCoordinator.setFirstLastName("Mendoza");
        testCoordinator.setSecondLastName("Juárez");
        testCoordinator.setEmail(uniqueEmail);
        testCoordinator.setPhoneNumber(uniquePhone);
        testCoordinator.setPassword("Pass123!");
        testCoordinator.setPersonalNumber(uniquePersonalNumber);
    }

    @Test
    @Order(1)
    @DisplayName("Debe devolver false si el número personal no existe en la base de datos")
    void testExistCoordinatorFalse() throws DAOException {
        boolean exists = coordinatorDAO.existCoordinator("NUNCA");
        assertFalse(exists);
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException al insertar Coordinador nulo")
    void testAddCoordinatorNullDTO() {
        assertThrows(DAOException.class, () -> coordinatorDAO.addCoordinator(null));
    }

    @Test
    @Order(3)
    @DisplayName("Debe insertar un coordinador exitosamente")
    void testAddCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @Order(4)
    @DisplayName("Debe devolver true si el coordinador existe y está activo")
    void testExistCoordinatorTrue() throws DAOException {
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertTrue(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al insertar coordinador con número personal duplicado")
    void testAddCoordinatorDuplicatePersonalNumber() throws DAOException {
        CoordinatorDTO duplicate = new CoordinatorDTO();
        duplicate.setPersonalNumber(testCoordinator.getPersonalNumber());
        duplicate.setEmail("leo" + uniqueSuffix + "@gmail.com");
        duplicate.setFirstName("Leo");
        duplicate.setFirstLastName("Martínez");
        duplicate.setPhoneNumber("1234567890");
        duplicate.setPassword("Pass123!");

        assertThrows(DAOException.class, () -> coordinatorDAO.addCoordinator(duplicate));
    }

    @Test
    @Order(6)
    @DisplayName("Debe inactivar un coordinador exitosamente")
    void testInactivateCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertTrue(result);
        boolean existsActive = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertFalse(existsActive);
    }

    @Test
    @Order(7)
    @DisplayName("Debe devolver false si el coordinador existe pero está inactivo")
    void testExistCoordinatorInactive() throws DAOException {
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertFalse(exists);
    }

    @Test
    @Order(8)
    @DisplayName("Debe lanzar DAOException al inactivar un coordinador inexistente")
    void testInactivateCoordinatorNotFound() {
        CoordinatorDTO fakeCoordinator = new CoordinatorDTO();
        fakeCoordinator.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> coordinatorDAO.inactivateCoordinator(fakeCoordinator));
    }

    @Test
    @Order(9)
    @DisplayName("Debe activar un coordinador exitosamente")
    void testActivateCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.activateCoordinator(testCoordinator);
        assertTrue(result);
        boolean existsActive = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertTrue(existsActive);
    }

    @Test
    @Order(10)
    @DisplayName("Debe lanzar DAOException al activar un coordinador inexistente")
    void testActivateCoordinatorNotFound() {
        CoordinatorDTO fakeCoordinator = new CoordinatorDTO();
        fakeCoordinator.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> coordinatorDAO.activateCoordinator(fakeCoordinator));
    }

    @Test
    @Order(11)
    @DisplayName("Debe obtener lista de coordinadores activos (no nula)")
    void testObtainAllActiveCoordinatorsSuccess() throws DAOException {
        List <CoordinatorDTO> listCoordinator = coordinatorDAO.obtainAllActiveCoordinators();
        assertNotNull(listCoordinator);
    }

    @Test
    @Order(12)
    @DisplayName("Después de insertar un coordinador activo, debe aparecer en la lista")
    void testObtainAllActiveCoordinatorsIncludesNew() throws DAOException {
        testCoordinator.setPersonalNumber("C1111");
        testCoordinator.setEmail("nuevo_coord@gmail.com");
        coordinatorDAO.addCoordinator(testCoordinator);
        List<CoordinatorDTO> listCoordinator = coordinatorDAO.obtainAllActiveCoordinators();
        boolean found = listCoordinator.stream().anyMatch(coordinatorDTO ->
                coordinatorDTO.getPersonalNumber().equals(testCoordinator.getPersonalNumber()));
        assertTrue(found);
    }

    @Test
    @Order(13)
    @DisplayName("Después de inactivar, no debe aparecer en la lista de activos")
    void testObtainAllActiveCoordinatorsExcludesInactive() throws DAOException {
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        List<CoordinatorDTO> listCoordinator = coordinatorDAO.obtainAllActiveCoordinators();
        boolean found = listCoordinator.stream().anyMatch(coordinatorDTO ->
                coordinatorDTO.getPersonalNumber().equals(testCoordinator.getPersonalNumber()));
        assertFalse(found);
    }
}