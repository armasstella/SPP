package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ProfessionalPracticeEnrollmentDAOTest {

    private ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO;
    private ProfessionalPracticeEnrollmentDTO testProfessionalPracticeEnrollment;
    private CourseDTO courseDTO;
    private InternDTO internDTO;

    @BeforeAll
    void setUpAll() {
        professionalPracticeEnrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
        testProfessionalPracticeEnrollment = new ProfessionalPracticeEnrollmentDTO();
        courseDTO = new CourseDTO();
        internDTO = new InternDTO();
    }

    @BeforeEach
    void setUpEach() {
        courseDTO.setCourseCode(77777);
        testProfessionalPracticeEnrollment.setCourseDTO(courseDTO);
        internDTO.setId(1);
        internDTO.setStudentNumber("S24013315");
        testProfessionalPracticeEnrollment.setInternDTO(internDTO);

        testProfessionalPracticeEnrollment.setFinalGrade(0);
        testProfessionalPracticeEnrollment.setCoveredHours(0);
    }

    @Test
    @Order(1)
    @DisplayName("Debe lanzar DAOException al recibir un DTO nulo (Fail-Fast)")
    void testAddEnrollmentNullDTO() {
        assertThrows(DAOException.class, () -> professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException si la matrícula a asignar proyecto está vacía")
    void testAssignProjectEmptyStudentNumber() {
        assertThrows(DAOException.class, () -> professionalPracticeEnrollmentDAO.assignProjectToInscription(
                "", 1));
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException por llave foránea inválida al insertar")
    void testAddEnrollmentInvalidFK() {
        InternDTO intern = new InternDTO();
        intern.setId(99999);
        intern.setStudentNumber("S99999999");
        testProfessionalPracticeEnrollment.setInternDTO(intern);
        assertThrows(DAOException.class, () -> professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment));
    }

    @Test
    @Order(4)
    @DisplayName("Debe intentar insertar inscripción sin proyecto asignado")
    void testAddEnrollmentWithoutProject() throws DAOException {
        boolean result = professionalPracticeEnrollmentDAO.addProfessionalPracticeEnrollment(
                testProfessionalPracticeEnrollment);
        assertTrue(result);
    }

    @Test
    @Order(5)
    @DisplayName("Debe intentar asignar un proyecto a la inscripción (Depende de BD)")
    void testAssignProjectSuccess() throws DAOException {
        boolean result = professionalPracticeEnrollmentDAO.assignProjectToInscription("S24013315", 1);
        assertTrue(result);
    }
}