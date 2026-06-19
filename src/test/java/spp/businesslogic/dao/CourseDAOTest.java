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
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class CourseDAOTest {

    private CourseDAO courseDAO;
    private CourseDTO testCourse;

    @BeforeAll
    void setUpAll() {
        courseDAO = new CourseDAO();
        testCourse = new CourseDTO();
    }

    @BeforeEach
    void setUpEach() {
        int randomCourseCode = (int) (Math.random() * 89999) + 10000;

        testCourse.setCourseCode(randomCourseCode);
        testCourse.setTerm("AGOSTO-DICIEMBRE 2026");
        testCourse.setSchoolBlock(1);
        testCourse.setSection(1);
        testCourse.setCapacity(25);
        testCourse.setCourseDetails("Aula 105");
        testCourse.setInstructorDTO(null);
    }

    @Test
    @Order(1)
    @DisplayName("Debe lanzar DAOException si se intenta agregar un Curso nulo")
    void testAddCourseNullDTO() {
        assertThrows(DAOException.class, () -> courseDAO.addCourse(null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException si se intenta asignar profesor nulo")
    void testAssignInstructorNullDTO() {
        assertThrows(DAOException.class, () -> courseDAO.assignInstructorToCourse(null));
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException si se asigna un curso pero el Profesor es nulo")
    void testAssignInstructorWithNullInstructorObject() {
        assertThrows(DAOException.class, () -> courseDAO.assignInstructorToCourse(testCourse));
    }

    @Test
    @Order(4)
    @DisplayName("Debe insertar una experiencia educativa exitosamente (sin profesor)")
    void testAddCourseSuccess() throws DAOException {
        boolean result = courseDAO.addCourse(testCourse);
        assertTrue(result);
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al insertar un curso con NRC duplicado")
    void testAddCourseDuplicateNRC() throws DAOException {
        courseDAO.addCourse(testCourse);
        assertThrows(DAOException.class, () -> courseDAO.addCourse(testCourse));
    }

    @Test
    @Order(6)
    @DisplayName("Debe obtener la lista de experiencias educativas (no nula)")
    void testObtainAllActiveCourses() throws DAOException {
        List<CourseDTO> list = courseDAO.obtainAllActiveCourses();
        assertNotNull(list);
    }
}