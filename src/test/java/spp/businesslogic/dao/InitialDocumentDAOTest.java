package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InitialDocumentDAOTest {

    private InitialDocumentDAO documentDAO;
    private InitialDocumentDTO testDocument;
    private String validStudentNumber;

    @BeforeAll
    void setUpAll() {
        documentDAO = new InitialDocumentDAO();
        testDocument = new InitialDocumentDTO();
    }

    @BeforeEach
    void setUp() {
        validStudentNumber = "S24013314";

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String shortSuffix = uniqueSuffix.substring(uniqueSuffix.length() - 5);

        testDocument.setOriginalName("horario_clases_" + shortSuffix + ".pdf");
        testDocument.setSavedName("UUID-" + uniqueSuffix + ".pdf");
        testDocument.setFilePath("/docs/horarios/UUID-" + uniqueSuffix + ".pdf");
        testDocument.setSizeMb(2.5);
        testDocument.setExtension(".pdf");
        testDocument.setUploadDate(LocalDateTime.now());
        testDocument.setDocumentType("Horario de Clases");
    }

    @Test
    @DisplayName("Debe guardar un documento inicial exitosamente")
    void testSaveDocumentSuccess() throws DAOException {
        boolean result = documentDAO.saveDocument(validStudentNumber, testDocument);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe ejecutar la búsqueda de horario sin lanzar excepciones")
    void testSearchClassScheduleSuccess() {
        assertDoesNotThrow(() -> {
            boolean hasSchedule = documentDAO.searchClassScheduleForIntern(validStudentNumber);
            assertNotNull(hasSchedule);
        });
    }

    @Test
    @DisplayName("Debe ejecutar la búsqueda de calendarización sin lanzar excepciones")
    void testSearchActivitiesScheduleSuccess() {
        assertDoesNotThrow(() -> {
            boolean hasActivities = documentDAO.searchActivitiesScheduleForIntern(validStudentNumber);
            assertNotNull(hasActivities);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la matrícula es nula al guardar")
    void testSaveDocumentFailedNullStudentNumber() {
        assertThrows(DAOException.class, () -> {
            documentDAO.saveDocument(null, testDocument);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException si el DTO no tiene los datos obligatorios")
    void testSaveDocumentFailedMissingData() {
        testDocument.setOriginalName(null);
        assertThrows(DAOException.class, () -> {
            documentDAO.saveDocument(validStudentNumber, testDocument);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la matrícula proporcionada no existe en la BD")
    void testSaveDocumentFailedNonExistentIntern() {
        assertThrows(DAOException.class, () -> {
            documentDAO.saveDocument("S99999999", testDocument);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la matrícula es nula al buscar horario")
    void testSearchClassScheduleFailedNullStudentNumber() {
        assertThrows(DAOException.class, () -> {
            documentDAO.searchClassScheduleForIntern(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la matrícula es nula al buscar calendarización")
    void testSearchActivitiesScheduleFailedNullStudentNumber() {
        assertThrows(DAOException.class, () -> {
            documentDAO.searchActivitiesScheduleForIntern(null);
        });
    }
}