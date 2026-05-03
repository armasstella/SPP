package spp.businesslogic.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectManagerDAO;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectManagerDAOTest {

    private ProjectManagerDAO projectManagerDAO;
    private ProjectManagerDTO testProjectManager;

    @BeforeAll
    void setupAll() {
        projectManagerDAO = new ProjectManagerDAO();
    }

    @BeforeEach
    void setup() {
        testProjectManager = new ProjectManagerDTO();
        testProjectManager.setFirstName("Manager");
        testProjectManager.setSecondName(" ");
        testProjectManager.setFirstLastName("1");
        testProjectManager.setSecondLastName(" ");
        testProjectManager.setRole("Manager");
        testProjectManager.setResponsability("Managering");
        testProjectManager.setPhoneNumber("9242493621");
    }

    @Test
    @DisplayName("Debe insertar un encargado de proyecto exitosamente")
    void testAddProjectManagerSuccess() throws DAOException {
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result, "El método debería retornar true al insertara exitosamente");
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un duplicado")
    void testAddProjectManagerFailedDuplicatedData() throws DAOException {
        projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertThrows(DAOException.class, () ->
            projectManagerDAO.addProjectManagerDAO(testProjectManager));
    }
}
