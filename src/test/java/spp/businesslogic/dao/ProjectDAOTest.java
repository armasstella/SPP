package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ProjectDAOTest {

    private ProjectDAO projectDAO;
    private ProjectDTO testProject;
    private LinkedOrganizationDTO linkedOrganization;
    private ProjectManagerDTO projectManager;

    @BeforeAll
    void setUpAll() {
        projectDAO = new ProjectDAO();
        testProject = new ProjectDTO();
        linkedOrganization = new LinkedOrganizationDTO();
        projectManager = new ProjectManagerDTO();
    }

    @BeforeEach
    void setUpEach() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        testProject.setName("Proyecto Prueba " + uniqueSuffix.substring(uniqueSuffix.length() - 8));
        testProject.setDescription("Descripción de prueba para el sistema");
        testProject.setPlacesAvailable(5);

        linkedOrganization.setId(1);
        testProject.setLinkedOrganizationDTO(linkedOrganization);

        projectManager.setId(1);
        testProject.setProjectManagerDTO(projectManager);
    }

    @Test
    @Order(1)
    @DisplayName("Debe lanzar DAOException si se intenta agregar un proyecto nulo")
    void testAddProjectNullProject() {
        assertThrows(DAOException.class, () -> projectDAO.addProject(null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException si se intenta actualizar un proyecto nulo")
    void testUpdateProjectNullDTO() {
        assertThrows(DAOException.class, () -> projectDAO.updateProject(null));
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException si se intenta eliminar un proyecto nulo")
    void testDeleteProjectNullDTO() {
        assertThrows(DAOException.class, () -> projectDAO.deleteProject(null));
    }

    @Test
    @Order(4)
    @DisplayName("Debe insertar un proyecto exitosamente")
    void testAddProjectSuccess() throws DAOException {
        boolean result = projectDAO.addProject(testProject);
        assertTrue(result);
    }

    @Test
    @Order(5)
    @DisplayName("Debe obtener la lista general de proyectos (no nula)")
    void testObtainAllProjects() throws DAOException {
        var list = projectDAO.obtainAllProjects();
        assertNotNull(list);
    }

    @Test
    @Order(6)
    @DisplayName("Debe verificar si existe la cantidad mínima de proyectos")
    void testVerifyMinimumProjects() {
        assertDoesNotThrow(() -> projectDAO.verifyMinimumProjects());
    }

    @Test
    @Order(7)
    @DisplayName("Debe lanzar DAOException si la llave foránea de la Organización no existe")
    void testAddProjectInvalidOrganizationFK() {
        LinkedOrganizationDTO fakeLinkedOrganization = new LinkedOrganizationDTO();
        fakeLinkedOrganization.setId(99999);
        testProject.setLinkedOrganizationDTO(fakeLinkedOrganization);

        assertThrows(DAOException.class, () -> projectDAO.addProject(testProject));
    }

    @Test
    @Order(8)
    @DisplayName("Debe obtener lista de proyectos seleccionados por practicante")
    void testObtainSelectedProjectsByIntern() throws DAOException {
        List<ProjectDTO> list = projectDAO.obtainSelectedProjectsByIntern("S24013315");
        assertNotNull(list);
    }
}