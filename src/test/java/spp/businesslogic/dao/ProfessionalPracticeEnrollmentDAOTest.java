package spp.businesslogic.dao;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfessionalPracticeEnrollmentDAOTest {

    private ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO;
    private ProfessionalPracticeEnrollmentDTO testProfessionalPracticeEnrollment;
    private InternDTO internDTO;
    private ProjectDTO projectDTO;
    private CourseDTO courseDTO;

    @BeforeAll
    void setUpAll() {
        professionalPracticeEnrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
        testProfessionalPracticeEnrollment = new ProfessionalPracticeEnrollmentDTO();
        internDTO = new InternDTO();
        projectDTO = new ProjectDTO();
        courseDTO = new CourseDTO();
    }


    @BeforeEach
    void setUp() {
        internDTO.setId(1);
        internDTO.setStudentNumber("S24013315");
        projectDTO.setId(1);
        courseDTO.setCourseCode(88978);

        testProfessionalPracticeEnrollment.setInternDTO(internDTO);
        testProfessionalPracticeEnrollment.setFinalGrade(10);
        testProfessionalPracticeEnrollment.setProjectDTO(projectDTO);
        testProfessionalPracticeEnrollment.setCoveredHours(480);
        testProfessionalPracticeEnrollment.setCourseDTO(courseDTO);
    }

    @Test
    @DisplayName("Debe insertar una inscripción exitosamente")
    void testRegisterProfessionalPracticeEnrollmentSuccess() throws DAOException {
        boolean result = professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
            testProfessionalPracticeEnrollment);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el curso no existe")
    void testRegisterProfessionalPracticeEnrollmentFailedInvalidCourse() throws DAOException {
        testProfessionalPracticeEnrollment.getCourseDTO().setCourseCode(99999);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el curso no se asigna")
    void testRegisterProfessionalPracticeEnrollmentFailedMissingCourse() throws DAOException {
        testProfessionalPracticeEnrollment.setCourseDTO(null);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                    testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el proyecto no existe")
    void testRegisterProfessionalPracticeEnrollmentFailedInvalidProject() throws DAOException {
        testProfessionalPracticeEnrollment.getProjectDTO().setId(555);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debería lanzar DAOException cuando no hay un proyecto asignado")
    void testRegisterProfessionalPracticeEnrollmentFailedMissingProject()  throws DAOException {
        testProfessionalPracticeEnrollment.setProjectDTO(null);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                    testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el practicante no existe")
    void testRegisterProfessionalPracticeEnrollmentFailedInvalidIntern() throws DAOException {
        testProfessionalPracticeEnrollment.getInternDTO().setStudentNumber("S28014410");
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debería lanzar DAOException cuando no hay un practicante asignado")
    void testRegisterProfessionalPracticeEnrollmentFailedMissingIntern() throws DAOException {
        testProfessionalPracticeEnrollment.setInternDTO(null);
        assertThrows(DAOException.class, () -> {
            professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        });
    }

    @Test
    @DisplayName("Debería lanzar permitir ingresar las horas cubiertas ingresadas")
    void testAddLinkedOrganizationWithCoveredHoursSuccess() throws DAOException {
        testProfessionalPracticeEnrollment.setCoveredHours(66);
        boolean result =
                professionalPracticeEnrollmentDAO.registerProfessionalPracticeEnrollment(
                        testProfessionalPracticeEnrollment);
        assertTrue(result);
    }
}
