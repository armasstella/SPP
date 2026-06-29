package spp.businesslogic.dao;

import org.junit.jupiter.api.*;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrioritizedProjectDAOTest {

    private PrioritizedProjectDAO prioritizedProjectDAO;
    private List<ProjectDTO> testProjects;

    private final String VALID_STUDENT_EMAIL = "estudiante.prueba@test.com";

    @BeforeAll
    void setupAll() {
        prioritizedProjectDAO = new PrioritizedProjectDAO();
        testProjects = new ArrayList<>();

        ProjectDTO project1 = new ProjectDTO(); project1.setId(1);
        ProjectDTO project2 = new ProjectDTO(); project2.setId(2);
        testProjects.add(project1);
        testProjects.add(project2);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Verificar existencia mediante correo")
    void testFindPrioritizedProjectsByInternEmail() throws DAOException {
        boolean exists = prioritizedProjectDAO.findPrioritizedProjectsByInternEmail(VALID_STUDENT_EMAIL);
        assertTrue(exists, "El sistema debería reconocer que el alumno ya priorizó proyectos.");
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Recuperar lista de proyectos priorizados y comparar")
    void testFindPrioritizedProjectsIdentifiers() throws DAOException {
        String VALID_STUDENT_NUMBER = "S22513740";
        List<ProjectDTO> retrieved = prioritizedProjectDAO.findPrioritizedProjectsIdentifiersByStudentNumber(VALID_STUDENT_NUMBER);

        assertNotNull(retrieved);
        assertEquals(2, retrieved.size());
        assertEquals(testProjects.get(0).getId(), retrieved.get(0).getId());
    }

    @Test
    @Order(4)
    @DisplayName("Excepción: Intentar priorizar proyectos ya priorizados")
    void testSaveDuplicatePrioritizedProjects() {
        assertThrows(DAOException.class, () -> {
            prioritizedProjectDAO.savePrioritizedProjects(VALID_STUDENT_EMAIL, testProjects);
        }, "Debería lanzar DAOException porque los proyectos ya existen.");
    }
}