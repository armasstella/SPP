package spp.businesslogic.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProfessionalPracticeEnrollmentDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ProfessionalPracticeEnrollmentDAOTest {

    private ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO;
    private ProfessionalPracticeEnrollmentDTO testProfessionalPracticeEnrollment;

    @BeforeAll
    void setUpAll() {
        professionalPracticeEnrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
    }

    @BeforeEach
    void setUp() {
        InternDTO intern = new InternDTO();
        intern.setId(1);
        intern.setStudentNumber("S24013315");
        InstructorDTO instructor = new InstructorDTO();
        instructor.setId(1);
        instructor.setPersonalNumber("55155");
        ProjectDTO project = new ProjectDTO();
        project.setId(1);

        testProfessionalPracticeEnrollment = new ProfessionalPracticeEnrollmentDTO();
        testProfessionalPracticeEnrollment.setNrc("1212A");
        testProfessionalPracticeEnrollment.setTerm("Febrero - Julio 2026");
        testProfessionalPracticeEnrollment.setInstructorDTO(instructor);
        testProfessionalPracticeEnrollment.setInternDTO(intern);
        testProfessionalPracticeEnrollment.setFinalGrade(10);
        testProfessionalPracticeEnrollment.setProjectDTO(project);
        testProfessionalPracticeEnrollment.setCoveredHours(480);
    }

    @Test
    @DisplayName("Debe insertar una inscripción exitosamente")
    void testAddProfessionalPracticeEnrollmentSuccess() throws DAOException {
        boolean result = professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
            testProfessionalPracticeEnrollment);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el profesor no existe")
    void testAddProfessionalPracticeEnrollmentFailedInvalidInstructor() throws DAOException {
        testProfessionalPracticeEnrollment.getInstructorDTO().setId(555);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debería lanzar DAOException al insertar un profesor que no existe");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el proyecto no existe")
    void testAddProfessionalPracticeEnrollmentFailedInvalidProject() throws DAOException {
        testProfessionalPracticeEnrollment.getProjectDTO().setId(555);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debería lanzar DAOException al insertar un proyecto que no existe");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el practicante no existe")
    void testAddProfessionalPracticeEnrollmentFailedInvalidIntern() throws DAOException {
        testProfessionalPracticeEnrollment.getInternDTO().setId(555);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debería lanzar DAOException al insertar un practicante que no existe");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando no hay un profesor asignado")
    void testAddProfessionalPracticeEnrollmentFailedMissingInstructor() throws DAOException {
        testProfessionalPracticeEnrollment.setInstructorDTO(null);

        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debería lanzar DAOException cuando no se le asigna un profesor a la inscripción");
    }

    @Test
    @DisplayName("Debería lanzar DAOException cuando no hay un practicante asignado")
    void testAddProfessionalPracticeEnrollmentFailedMissingIntern() throws DAOException {
        testProfessionalPracticeEnrollment.setInternDTO(null);

        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debe lanzar DAOException cuando no se le asigna un practicante a la inscripción");
    }

    @Test
    @DisplayName("Debería lanzar DAOException cuando no hay un proyecto asignado")
    void testAddProfessionalPracticeEnrollmentFailedMissingProject()  throws DAOException {
        testProfessionalPracticeEnrollment.setProjectDTO(null);

        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debe lanzar DAOException cuando no se le asigna un proyecto a la inscripción");
    }

    @Test
    @DisplayName("Debería lanzar DAOException cuando no hay un periodo ingresado")
    void testProfessionalPracticeEnrollmentFailedMissingInstructor() throws DAOException {
        testProfessionalPracticeEnrollment.setTerm(null);

        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        }, "Debe lanzar DAOException cuando no se le asigna un periodo a la inscripción");
    }
}
