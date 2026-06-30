package spp.businesslogic.dao;

import org.junit.jupiter.api.*;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseDAOTest {

    private CourseDAO courseDAO;
    private CourseDTO testCourse;
    private final int ACTIVE_TERM_ID = 1;

    @BeforeAll
    void setupAll() {
        courseDAO = new CourseDAO();
        testCourse = new CourseDTO();

        int uniqueNrc = 10000 + (int) (Math.random() * 90000);
        testCourse.setCourseCode(uniqueNrc);
        testCourse.setSchoolBlock(1);
        testCourse.setSection(1);
        testCourse.setCapacity(30);
        testCourse.setCourseDetails("Experiencia Educativa de prueba");
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Registro de curso exitoso")
    void testRegisterCourseSuccess() throws DAOException {
        boolean result = courseDAO.registerCourse(testCourse, ACTIVE_TERM_ID);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Registro de curso con NRC duplicado")
    void testRegisterCourseDuplicateNrc() {
        assertThrows(DAOException.class, () -> {
            courseDAO.registerCourse(testCourse, ACTIVE_TERM_ID);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Asignación de instructor inexistente debería devolver false")
    void testAssignInstructorNotFound() throws DAOException {
        InstructorDTO fakeInstructor = new InstructorDTO();
        fakeInstructor.setId(99999);
        fakeInstructor.setPersonalNumber("FAKE000");
        testCourse.setInstructorDTO(fakeInstructor);

        boolean result = courseDAO.assignInstructorToCourse(testCourse);

        assertFalse(result);
    }
}