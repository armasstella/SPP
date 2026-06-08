package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseDAOTest {

    private CourseDAO courseDAO;
    private CourseDTO testCourse;
    private InstructorDTO instructorDTO;
    private int uniqueCourseCode;

    @BeforeAll
    void setUpAll() {
        courseDAO = new CourseDAO();
        testCourse = new CourseDTO();
        instructorDTO = new InstructorDTO();
    }

    @BeforeEach
    void setUp() throws DAOException {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String courseCodeString = "8" + uniqueSuffix.substring(uniqueSuffix.length() - 4);
        uniqueCourseCode = Integer.parseInt(courseCodeString);
        instructorDTO.setId(32);
        instructorDTO.setPersonalNumber("77777");

        testCourse.setCourseCode(uniqueCourseCode);
        testCourse.setTerm("FEB-JUL 2026");
        testCourse.setSchoolBlock(1);
        testCourse.setSection(2);
        testCourse.setCapacity(30);
        testCourse.setCourseDetails("Prácticas Profesionales");
        testCourse.setInstructorDTO(instructorDTO);
    }

    @Test
    @DisplayName("Debe insertar un curso con un profesor asignado exitosamente")
    void testAddCourseSuccess() throws DAOException {
        boolean result = courseDAO.addCourse(testCourse);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar un curso sin profesor asignado")
    void testAddCourseWithoutInstructorSuccess() throws DAOException {
        testCourse.setInstructorDTO(null);
        boolean result = courseDAO.addCourse(testCourse);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe recuperar la lista completa de cursos activos")
    void testObtainAllActiveCoursesSuccess() throws DAOException {
        courseDAO.addCourse(testCourse);
        List<CourseDTO> courses = courseDAO.obtainAllActiveCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
        assertNotNull(courses.get(0).getInstructorDTO().getFirstName());
    }

    @Test
    @DisplayName("Debe devolver true si hay cursos en el sistema")
    void testSearchCoursesSuccess() throws DAOException {
        courseDAO.addCourse(testCourse);
        boolean exists = courseDAO.searchCourses();
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe asignar exitosamente un profesor al curso")
    void testAssignInstructorToCourseSuccess() throws DAOException {
        testCourse.setInstructorDTO(null);
        courseDAO.addCourse(testCourse);

        int idCourse = courseDAO.obtainAllActiveCourses().stream()
                .filter(c -> c.getCourseCode() == uniqueCourseCode)
                .findFirst()
                .get()
                .getIdCourse();

        testCourse.setIdCourse(idCourse);
        testCourse.setInstructorDTO(instructorDTO);

        boolean result = courseDAO.assignInstructorToCourse(testCourse);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe permitir insertar un curso sin periodo")
    void testAddCourseNullTermSuccess() throws DAOException {
        testCourse.setTerm(null);
        boolean result = courseDAO.addCourse(testCourse);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un NRC duplicado")
    void testAddCourseFailedDuplicateCourseCode() throws DAOException {
        courseDAO.addCourse(testCourse);
        assertThrows(DAOException.class, () -> {
            courseDAO.addCourse(testCourse);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al intentar asignar un profesor nulo")
    void testAssignInstructorFailedNullInstructor() {
        testCourse.setInstructorDTO(null);
        assertThrows(DAOException.class, () -> {
            courseDAO.assignInstructorToCourse(testCourse);
        });
    }
}