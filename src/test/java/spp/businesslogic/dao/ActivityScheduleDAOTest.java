package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityScheduleDAOTest {

    private ActivityScheduleDAO activityScheduleDAO;
    private ActivityScheduleDTO testSchedule;

    @BeforeAll
    void setupAll() {
        activityScheduleDAO = new ActivityScheduleDAO();
        testSchedule = new ActivityScheduleDTO();

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        testSchedule.setOriginalName("Cronograma_Actividades_v1.pdf");
        testSchedule.setSavedName("SCH_" + uniqueSuffix + ".pdf");
        testSchedule.setFilePath("/docs/schedules/SCH_" + uniqueSuffix + ".pdf");
        testSchedule.setSizeMb(2.5);
        testSchedule.setExtension(".pdf");
        testSchedule.setUploadDate(LocalDateTime.now().withNano(0));
    }


    @Test
    @Order(1)
    @DisplayName("Excepción/Flujo Alterno: Guardar con un proyecto que no existe lanza DAOException")
    void testSaveActivityScheduleInvalidProject() {
        int invalidProjectId = 9999999;

        DAOException exception = assertThrows(DAOException.class, () -> {
            activityScheduleDAO.saveActivitySchedule(testSchedule, invalidProjectId);
        });

        assertTrue(exception.getMessage().contains("No se pudo guardar la calendarización"));
    }
}