package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.enums.ActivityType;
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

    private ActivityType activityType;

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
        activityType = ActivityType.MONTHLY;
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Alterno: Registrar actividad a una matrícula que no existe devuelve false")
    void testSaveActivityForInternAlternate() throws DAOException {
        boolean result = activityDAO.saveActivityForIntern("S00000000", testActivity, activityType);
        assertFalse(result);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Actualizar una actividad con ID inexistente devuelve false")
    void testUpdateActivityAlternate() throws DAOException {
        testActivity.setId(9999999);
        boolean result = activityDAO.updateActivity(testActivity);
        assertFalse(result);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Eliminar una actividad con ID inexistente devuelve false")
    void testDeleteActivityAlternate() throws DAOException {
        boolean result = activityDAO.deleteActivity(9999999);
        assertFalse(result);
    }
}