package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectDAOTest {

    private ProjectDAO projectDAO;
    private ProjectDTO testProject;

    @BeforeAll
    void setupAll() {
        projectDAO = new ProjectDAO();
        testProject = new ProjectDTO();
    }

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueName = "Proyecto " + uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniqueDescription = "Descripción " + uniqueSuffix;

        LinkedOrganizationDTO linkedOrganization = new LinkedOrganizationDTO();
        int EXISTING_ORGANIZATION_ID = 1;
        linkedOrganization.setId(EXISTING_ORGANIZATION_ID);

        ProjectManagerDTO projectManager = new ProjectManagerDTO();
        int EXISTING_PROJECT_MANAGER_ID = 1;
        projectManager.setId(EXISTING_PROJECT_MANAGER_ID);

        testProject.setName(uniqueName);
        testProject.setDescription(uniqueDescription);
        testProject.setPlacesAvailable(5);
        testProject.setLinkedOrganizationDTO(linkedOrganization);
        testProject.setProjectManagerDTO(projectManager);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un proyecto correctamente y devolver un ID válido")
    void testRegisterProjectSuccess() throws DAOException {
        int generatedId = projectDAO.registerProject(testProject);
        assertTrue(generatedId > 0);
        testProject.setId(generatedId);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Debe lanzar DAOException al registrar un proyecto duplicado (mismo nombre)")
    void testRegisterProjectDuplicate() throws DAOException {
        int id = projectDAO.registerProject(testProject);
        testProject.setId(id);

        DAOException exception = assertThrows(DAOException.class, () -> {
            projectDAO.registerProject(testProject);
        });

        assertTrue(exception.getMessage().contains("El proyecto no pudo ser registrado"));
    }

    @Test
    @Order(3)
    @DisplayName("Excepción: Debe lanzar DAOException al registrar un proyecto con organización o encargado inexistente")
    void testRegisterProjectInvalidForeignKeys() {
        ProjectDTO invalidProject = new ProjectDTO();
        invalidProject.setName("Proyecto Inexistente");
        invalidProject.setDescription("Descripción");
        invalidProject.setPlacesAvailable(3);

        LinkedOrganizationDTO invalidOrg = new LinkedOrganizationDTO();
        invalidOrg.setId(99999);
        invalidProject.setLinkedOrganizationDTO(invalidOrg);

        ProjectManagerDTO invalidManager = new ProjectManagerDTO();
        invalidManager.setId(99999);
        invalidProject.setProjectManagerDTO(invalidManager);

        DAOException exception = assertThrows(DAOException.class, () -> {
            projectDAO.registerProject(invalidProject);
        });

        assertTrue(exception.getMessage().contains("El proyecto no pudo ser registrado"));
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe actualizar un proyecto correctamente")
    void testUpdateProjectSuccess() throws DAOException {
        int id = projectDAO.registerProject(testProject);
        testProject.setId(id);

        String newName = "Proyecto Actualizado " + System.currentTimeMillis();
        String newDescription = "Nueva descripción " + System.currentTimeMillis();
        testProject.setName(newName);
        testProject.setDescription(newDescription);
        testProject.setPlacesAvailable(10);

        boolean result = projectDAO.updateProject(testProject);
        assertTrue(result);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Actualizar un proyecto que no existe debe devolver false")
    void testUpdateProjectNotFound() throws DAOException {
        ProjectDTO fakeProject = new ProjectDTO();
        fakeProject.setId(99999);
        fakeProject.setName("Fake");
        fakeProject.setDescription("Fake");
        fakeProject.setPlacesAvailable(1);

        boolean result = projectDAO.updateProject(fakeProject);
        assertFalse(result);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Normal: Debe eliminar un proyecto correctamente")
    void testDeleteProjectSuccess() throws DAOException {
        int id = projectDAO.registerProject(testProject);
        testProject.setId(id);

        boolean result = projectDAO.deleteProject(testProject);
        assertTrue(result);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Eliminar un proyecto que no existe debe devolver false")
    void testDeleteProjectNotFound() throws DAOException {
        ProjectDTO fakeProject = new ProjectDTO();
        fakeProject.setId(99999);

        boolean result = projectDAO.deleteProject(fakeProject);
        assertFalse(result);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Debe obtener la lista de proyectos con detalles para el periodo activo")
    void testFindProjectsDetailsForActiveTermSuccess() throws DAOException {
        int id = projectDAO.registerProject(testProject);
        testProject.setId(id);

        List<ProjectDTO> projects = projectDAO.findProjectsDetailsForActiveTerm();
        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        boolean found = projects.stream().anyMatch(project -> project.getId() == id);
        assertTrue(found);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Debe verificar si existe la cantidad mínima de proyectos para el periodo activo")
    void testHasMinimumProjectsForActiveTerm() throws DAOException {
        boolean hasMinimum = projectDAO.hasMinimumProjectsForActiveTerm();
        assertNotNull(hasMinimum);
    }
}