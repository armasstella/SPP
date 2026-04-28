package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ActivityDAO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityDAOTest {

    private ActivityDAO activityDAO;
    private ActivityDTO testActivity;

    @BeforeAll
    void setupAll() {
        activityDAO = new ActivityDAO();
    }

    @BeforeEach
    void setUp() {
        InstructorDTO instructor = new InstructorDTO();
        instructor.setId(7);
        instructor.setPersonalNumber("12720");

        testActivity = new ActivityDTO();
        testActivity.setTitle("Actividad de Prueba JUnit");
        testActivity.setDescription("Descripción detallada de la actividad de prueba.");
        testActivity.setSubmissionDate(LocalDateTime.now().plusDays(7));
        testActivity.setInstructorDTO(instructor);
    }

    @Test
    @DisplayName("Debe insertar una actividad exitosamente")
    void testAddActivitySuccess() throws DAOException {
        boolean result = activityDAO.addActivity(testActivity);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando el instructor no existe (violación FK)")
    void testAddActivityFailedInvalidInstructor() {
        testActivity.getInstructorDTO().setId(999999);

        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        }, "Debería lanzar DAOException por restricción de llave foránea");
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar título nulo")
    void testAddActivityFailedNullTitle() {
        testActivity.setTitle(null);

        assertThrows(DAOException.class, () -> {
            activityDAO.addActivity(testActivity);
        }, "La base de datos debería rechazar un título nulo");
    }

    @Test
    @DisplayName("Debe lanzar DAOException cuando no hay InstructorDTO asignado")
    void testAddActivityFailedMissingInstructorDTO() {
        testActivity.setInstructorDTO(null);

        assertThrows(Exception.class, () -> {
            activityDAO.addActivity(testActivity);
        });
    }
}