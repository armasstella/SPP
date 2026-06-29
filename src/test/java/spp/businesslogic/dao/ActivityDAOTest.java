package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityDAOTest {

    private ActivityDAO activityDAO;
    private ActivityDTO testActivity;

    private final String VALID_STUDENT_NUMBER = "S22513740";

    private int generatedActivityId = -1;
    private String uniqueTitle;

    @BeforeAll
    void setupAll() {
        activityDAO = new ActivityDAO();
        testActivity = new ActivityDTO();

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        uniqueTitle = "Actividad " + uniqueSuffix.substring(uniqueSuffix.length() - 6);

        testActivity.setTitle(uniqueTitle);
        testActivity.setDescription("Descripción de validación exacta.");
        testActivity.setStartDate(LocalDate.now());
        testActivity.setEndDate(LocalDate.now().plusDays(5));
        testActivity.setEstimatedTime(10);
        testActivity.setEffectiveTime(5);
        testActivity.setProgress(50);
        testActivity.setObservations("Observación de prueba.");
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar una actividad exitosamente")
    void testSaveActivityForInternSuccess() throws DAOException {
        boolean result = activityDAO.saveActivityForIntern(VALID_STUDENT_NUMBER, testActivity);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe obtener la lista y comparar que los datos exactos estén en la BD")
    void testFindActivitiesByStudentNumberEquals() throws DAOException {
        List<ActivityDTO> activities = activityDAO.findActivitiesByStudentNumber(VALID_STUDENT_NUMBER);

        assertNotNull(activities);
        assertFalse(activities.isEmpty());

        ActivityDTO retrievedActivity = activities.stream()
                .filter(activity -> activity.getTitle().equals(uniqueTitle))
                .findFirst()
                .orElse(null);

        assertNotNull(retrievedActivity);

        generatedActivityId = retrievedActivity.getId();

        assertEquals(testActivity.getTitle(), retrievedActivity.getTitle());
        assertEquals(testActivity.getDescription(), retrievedActivity.getDescription());
        assertEquals(testActivity.getStartDate(), retrievedActivity.getStartDate());
        assertEquals(testActivity.getEndDate(), retrievedActivity.getEndDate());
        assertEquals(testActivity.getEstimatedTime(), retrievedActivity.getEstimatedTime());
        assertEquals(testActivity.getEffectiveTime(), retrievedActivity.getEffectiveTime());
        assertEquals(testActivity.getProgress(), retrievedActivity.getProgress());
        assertEquals(testActivity.getObservations(), retrievedActivity.getObservations());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe actualizar la actividad y verificar los cambios en la BD")
    void testUpdateActivitySuccess() throws DAOException {
        testActivity.setId(generatedActivityId);
        testActivity.setTitle("Título Modificado");
        testActivity.setDescription("Descripción Modificada");
        testActivity.setProgress(100);

        boolean updateResult = activityDAO.updateActivity(testActivity);
        assertTrue(updateResult);

        List<ActivityDTO> activities = activityDAO.findActivitiesByStudentNumber(VALID_STUDENT_NUMBER);
        ActivityDTO updatedActivity = activities.stream()
                .filter(activity -> activity.getId() == generatedActivityId)
                .findFirst()
                .orElse(null);

        assertNotNull(updatedActivity);
        assertEquals("Título Modificado", updatedActivity.getTitle());
        assertEquals("Descripción Modificada", updatedActivity.getDescription());
        assertEquals(100, updatedActivity.getProgress());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe eliminar la actividad registrada")
    void testDeleteActivitySuccess() throws DAOException {
        boolean result = activityDAO.deleteActivity(generatedActivityId);
        assertTrue(result);

        List<ActivityDTO> activities = activityDAO.findActivitiesByStudentNumber(VALID_STUDENT_NUMBER);
        boolean exists = activities.stream().anyMatch(activity -> activity.getId() == generatedActivityId);
        assertFalse(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Registrar actividad a una matrícula que no existe devuelve false")
    void testSaveActivityForInternAlternate() throws DAOException {
        boolean result = activityDAO.saveActivityForIntern("S00000000", testActivity);
        assertFalse(result);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Actualizar una actividad con ID inexistente devuelve false")
    void testUpdateActivityAlternate() throws DAOException {
        testActivity.setId(9999999);
        boolean result = activityDAO.updateActivity(testActivity);
        assertFalse(result);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Eliminar una actividad con ID inexistente devuelve false")
    void testDeleteActivityAlternate() throws DAOException {
        boolean result = activityDAO.deleteActivity(9999999);
        assertFalse(result);
    }
}