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
import spp.dataaccess.dao.ProjectDAO;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectDAOTest {

    private ProjectDAO projectDAO;
    private ProjectDTO testProject;

    @BeforeAll
    void setUpAll() {
        projectDAO = new ProjectDAO();
    }

    @BeforeEach
    void setUpEach() {
        ProjectManagerDTO projectManager = new ProjectManagerDTO();
        projectManager.setId(1);
        LinkedOrganizationDTO linkedOrganization = new LinkedOrganizationDTO();
        linkedOrganization.setId(1);

        testProject = new ProjectDTO();
        testProject.setDescription(" ");
        testProject.setDisponibility(true);
        testProject.setProjectManagerDTO(projectManager);
        testProject.setLinkedOrganizationDTO(linkedOrganization);
    }

    @Test
    @DisplayName("Debe insertar un proyecto exitosamente")
    void testAddProjectSuccess() throws DAOException {
        boolean result = projectDAO.addProject(testProject);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");
    }
}
