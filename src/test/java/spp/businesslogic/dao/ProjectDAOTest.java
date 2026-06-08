package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.dto.LinkedOrganizationDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectDAOTest {

    private ProjectDAO projectDAO;
    private ProjectDTO testProject;
    private LinkedOrganizationDTO linkedOrganizationDTO;
    private ProjectManagerDTO projectManagerDTO;

    @BeforeAll
    void setUpAll() {
        projectDAO = new ProjectDAO();
        testProject = new ProjectDTO();
        linkedOrganizationDTO = new LinkedOrganizationDTO();
        projectManagerDTO = new ProjectManagerDTO();
    }

    @BeforeEach
    void setUpEach() {
        projectManagerDTO.setId(1);
        linkedOrganizationDTO.setId(1);

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        testProject.setName("Bolsa de Trabajo" + uniqueSuffix);
        testProject.setDescription("Desarrollo de un sistema web de la Universidad Mexicana " +
                "para la bolsa de trabajo");
        testProject.setAvailability("Disponible");
        testProject.setPlacesAvailable(2);
        testProject.setLinkedOrganizationDTO(linkedOrganizationDTO);
        testProject.setProjectManagerDTO(projectManagerDTO);
    }

    @Test
    @DisplayName("Debe insertar un proyecto exitosamente")
    void testAddProjectSuccess() throws DAOException {
        boolean result = projectDAO.addProject(testProject);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando no hay una organización vinculada asignada")
    void testAddProjectFailedMissingLinkedOrganization() throws DAOException {
        testProject.setLinkedOrganizationDTO(null);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando no hay un encargado de proyecto asignado")
    void testAddProjectFailedMissingProjectManager() throws DAOException {
        testProject.setProjectManagerDTO(null);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el encargado de proyecto no existe")
    void testAddProjectFailedInvalidProjectManager() throws DAOException {
        testProject.getProjectManagerDTO().setId(3987);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el nombre del proyecto es nulo")
    void testAddProjectFailedNullName() throws DAOException {
        testProject.setName(null);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando la descripción del proyecto es nula")
    void testAddProjectFailedNullDescription() throws DAOException {
        testProject.setDescription(null);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando la organización vinculada no existe")
    void testAddProjectFailedInvalidLinkedOrganization() throws DAOException {
        testProject.getLinkedOrganizationDTO().setId(12111);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el nombre del proyecto ya existe")
    void testAddProjectFailedDuplicateName() throws DAOException {
        projectDAO.addProject(testProject);
        assertThrows(DAOException.class, () -> {
            projectDAO.addProject(testProject);
        });
    }
}
