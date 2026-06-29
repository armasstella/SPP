package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.connection.MySQLConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectManagerDAOTest {

    private ProjectManagerDAO projectManagerDAO;
    private ProjectManagerDTO testProjectManager;
    private int existingOrganizationId;

    @BeforeAll
    void setupAll() throws SQLException {
        projectManagerDAO = new ProjectManagerDAO();
        testProjectManager = new ProjectManagerDTO();
        existingOrganizationId = getExistingOrganizationId();
    }

    private int getExistingOrganizationId() throws SQLException {
        String sqlQuery = "SELECT id_organizacion_vinculada FROM organizaciones_vinculadas LIMIT 1";
        try (Statement statement = MySQLConnection.getInstance().getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        throw new IllegalStateException("No hay organizaciones vinculadas en la BD. Insertar una primero.");
    }

    @BeforeEach
    void setUp() {
        String uniquePhone = "228" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        testProjectManager.setFirstName("Juan");
        testProjectManager.setSecondName("Carlos");
        testProjectManager.setFirstLastName("Perez");
        testProjectManager.setSecondLastName("Gomez");
        testProjectManager.setResponsibility("Coordinador");
        testProjectManager.setRole("Supervisor");
        testProjectManager.setPhoneNumber(uniquePhone);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un encargado de proyecto correctamente")
    void testRegisterProjectManagerSuccess() throws DAOException {
        boolean result = projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Debe lanzar DAOException al registrar un encargado de proyecto duplicado")
    void testRegisterProjectManagerDuplicate() throws DAOException {
        projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);

        DAOException exception = assertThrows(DAOException.class, () -> {
            projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);
        });

        assertTrue(exception.getMessage().contains("El encargado de proyecto ya existe") ||
                exception.getMessage().contains("duplicados"));
    }

    @Test
    @Order(3)
    @DisplayName("Excepción: Debe lanzar DAOException al registrar con organización inexistente")
    void testRegisterProjectManagerInvalidOrganization() {
        int invalidOrgId = 99999;
        DAOException exception = assertThrows(DAOException.class, () -> {
            projectManagerDAO.registerProjectManager(testProjectManager, invalidOrgId);
        });

        assertTrue(exception.getMessage().contains("organización vinculada no es válida") ||
                exception.getMessage().contains("El encargado de proyecto ya existe"));
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe obtener la lista de encargados de proyectos y verificar que el registrado esté presente")
    void testGetActiveProjectManagers() throws DAOException {
        projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);

        List<ProjectManagerDTO> managers = projectManagerDAO.getActiveProjectManagers();
        assertNotNull(managers);
        assertFalse(managers.isEmpty());

        String expectedFullName = "Juan Carlos Perez Gomez";
        boolean found = managers.stream()
                .anyMatch(manager -> expectedFullName.equals(manager.getFirstName()));

        assertTrue(found);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Debe verificar que existen encargados de proyectos (devuelve true)")
    void testExistsProjectManagersTrue() throws DAOException {
        projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);

        boolean exists = projectManagerDAO.existsProjectManagers();
        assertTrue(exists);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe verificar que existen encargados (devuelve false) - si la BD está vacía")
    void testExistsProjectManagersFalse() throws DAOException {
        assertDoesNotThrow(() -> projectManagerDAO.existsProjectManagers());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Debe obtener los encargados de proyectos por organización")
    void testGetProjectManagersByOrganization() throws DAOException {
        projectManagerDAO.registerProjectManager(testProjectManager, existingOrganizationId);

        List<ProjectManagerDTO> managers = projectManagerDAO.getProjectManagersByOrganization(existingOrganizationId);
        assertNotNull(managers);
        assertFalse(managers.isEmpty());

        String expectedFullName = "Juan Carlos Perez Gomez";
        boolean found = managers.stream()
                .anyMatch(manager -> expectedFullName.equals(manager.getFirstName()));

        assertTrue(found);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Obtener encargados de una organización sin registros (debe devolver lista vacía)")
    void testGetProjectManagersByOrganizationEmpty() throws DAOException {
        int nonExistentOrgId = 99999;
        List<ProjectManagerDTO> managers = projectManagerDAO.getProjectManagersByOrganization(nonExistentOrgId);
        assertNotNull(managers);
        assertTrue(managers.isEmpty());
    }
}