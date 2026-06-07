package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityDAOTest {

    private ActivityDAO activityDAO;
    private ActivityDTO testActivity;
    private InstructorDTO instructorDTO;

    @BeforeAll
    void setupAll() {
        activityDAO = new ActivityDAO();
        testActivity = new ActivityDTO();
        instructorDTO = new InstructorDTO();
    }

    @BeforeEach
    void setUp() {
        instructorDTO.setId(7);
        instructorDTO.setPersonalNumber("12720");

        testActivity.setTitle("Actividad de Prueba JUnit");
        testActivity.setDescription("Descripción detallada de la actividad de prueba.");
        testActivity.setSubmissionDate(LocalDateTime.now().plusDays(7));
        testActivity.setInstructorDTO(instructorDTO);
    }

    @Test
    @DisplayName("Debe insertar una actividad exitosamente")
    void testAddActivitySuccess() throws DAOException {
        boolean result = activityDAO.addActivity(testActivity);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe conservar el título al registrar una actividad")
    void testAddActivityStoresCorrectTitle() throws DAOException {
        activityDAO.addActivity(testActivity);
        assertNotNull(testActivity.getTitle());
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el instructor no existe (violación FK)")
    void testAddActivityFailedInvalidInstructor() {
        testActivity.getInstructorDTO().setId(999999);
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar título nulo")
    void testAddActivityFailedNullTitle() {
        testActivity.setTitle(null);
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando no hay Instructor asignado")
    void testAddActivityFailedMissingInstructorDTO() throws  DAOException {
        testActivity.setInstructorDTO(null);
        assertThrows(Exception.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Debe haber error al insertar descripcion vacia")
    void testAddActivityWithEmptyDescriptionFails() throws DAOException {
        testActivity.setDescription(null);
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Deber haber error al insertar fecha vacia")
    void testAddActivityWithNullDeadlineFails() throws DAOException {
        testActivity.setSubmissionDate(null);
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Debe marcar error al insertar intructor sin identificación oficial")
    void testAddActivityFailedInstructorWithoutId() throws DAOException {
        instructorDTO.setPersonalNumber(null);
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el id y número de personal no coinciden")
    void testAddActivityFailedMismatchedInstructorData() throws DAOException {
        instructorDTO.setId(7);
        instructorDTO.setPersonalNumber("99999");
        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }
}