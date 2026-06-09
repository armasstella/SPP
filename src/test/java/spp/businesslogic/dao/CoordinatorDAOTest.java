package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoordinatorDAOTest {

    private CoordinatorDAO coordinatorDAO;
    private CoordinatorDTO testCoordinator;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        coordinatorDAO = new CoordinatorDAO();
        testCoordinator = new CoordinatorDTO();
    }

    @BeforeEach
    void setUpEach() {
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
    @DisplayName("Debe insertar un coordinador exitosamente")
    void testAddCoordinatorSuccess() throws DAOException {
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar coordinador con segundo nombre nulo (opcional)")
    void testAddCoordinatorWithNullSecondName() throws DAOException {
        testCoordinator.setSecondName(null);
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar coordinador con segundo apellido vacío")
    void testAddCoordinatorWithEmptySecondLastName() throws DAOException {
        testCoordinator.setSecondLastName("");
        boolean result = coordinatorDAO.addCoordinator(testCoordinator);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar coordinador con número personal duplicado")
    void testAddCoordinatorDuplicatePersonalNumber() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
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
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con número personal inválido " +
            "(minúsculas)")
    void testAddCoordinatorInvalidPersonalNumberFormatLowercase() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPersonalNumber("abc12");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con número personal demasiado corto")
    void testAddCoordinatorInvalidPersonalNumberTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPersonalNumber("ABCD");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con número personal demasiado largo")
    void testAddCoordinatorInvalidPersonalNumberTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPersonalNumber("ABCDEF");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con número personal vacío")
    void testAddCoordinatorNullPersonalNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPersonalNumber(null);
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe inactivar un coordinador exitosamente")
    void testInactivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        boolean result = coordinatorDAO.inactivateCoordinator(testCoordinator);
        assertTrue(result);
        boolean existsActive = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertFalse(existsActive);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al inactivar un coordinador con número personal inexistente")
    void testInactivateCoordinatorNotFound() {
        CoordinatorDTO fake = new CoordinatorDTO();
        fake.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> coordinatorDAO.inactivateCoordinator(fake));
    }

    @Test
    @DisplayName("Debe activar un coordinador exitosamente")
    void testActivateCoordinatorSuccess() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean result = coordinatorDAO.activateCoordinator(testCoordinator);
        assertTrue(result);
        boolean existsActive = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertTrue(existsActive);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al activar un coordinador inexistente")
    void testActivateCoordinatorNotFound() {
        CoordinatorDTO fake = new CoordinatorDTO();
        fake.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> coordinatorDAO.activateCoordinator(fake));
    }

    @Test
    @DisplayName("Debe devolver true si el coordinador existe y está activo")
    void testExistCoordinatorTrue() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe devolver false si el coordinador existe pero está inactivo")
    void testExistCoordinatorInactive() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        boolean exists = coordinatorDAO.existCoordinator(testCoordinator.getPersonalNumber());
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debe devolver false si el número personal no existe")
    void testExistCoordinatorFalse() throws DAOException {
        boolean exists = coordinatorDAO.existCoordinator("NUNCA");
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debe obtener lista de coordinadores activos (puede estar vacía)")
    void testObtainAllActiveCoordinatorsSuccess() throws DAOException {
        var list = coordinatorDAO.obtainAllActiveCoordinators();
        assertNotNull(list);
    }

    @Test
    @DisplayName("Después de insertar un coordinador activo, debe aparecer en la lista")
    void testObtainAllActiveCoordinatorsIncludesNew() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        var list = coordinatorDAO.obtainAllActiveCoordinators();
        boolean found = list.stream().anyMatch(c ->
                c.getPersonalNumber().equals(testCoordinator.getPersonalNumber()));
        assertTrue(found);
    }

    @Test
    @DisplayName("Después de inactivar, no debe aparecer en la lista de activos")
    void testObtainAllActiveCoordinatorsExcludesInactive() throws DAOException {
        coordinatorDAO.addCoordinator(testCoordinator);
        coordinatorDAO.inactivateCoordinator(testCoordinator);
        var list = coordinatorDAO.obtainAllActiveCoordinators();
        boolean found = list.stream().anyMatch(c -> c.getPersonalNumber().equals(testCoordinator.getPersonalNumber()));
        assertFalse(found);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al exceder longitud máxima en número personal (más de 5)")
    void testSetPersonalNumberExceedsMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testCoordinator.setPersonalNumber("ABCDEF"));
    }

    @Test
    @DisplayName("Debe aceptar número personal con longitud máxima de 5 caracteres")
    void testSetPersonalNumberMaxLength() {
        assertDoesNotThrow(() -> testCoordinator.setPersonalNumber("ABCDE"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con email inválido")
    void testAddCoordinatorInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setEmail("correo-sin-arroba");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con teléfono de menos de 10 dígitos")
    void testAddCoordinatorInvalidPhoneShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPhoneNumber("123");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar coordinador con contraseña débil")
    void testAddCoordinatorWeakPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCoordinator.setPassword("pass");
            coordinatorDAO.addCoordinator(testCoordinator);
        });
    }
}