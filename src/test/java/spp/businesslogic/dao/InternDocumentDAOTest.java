package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.InternDocumentDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InternDocumentDAOTest {

    private InternDocumentDAO internDocumentDAO;
    private InternDocumentDTO testDocument;

    private final String VALID_STUDENT_EMAIL = "estudiante.prueba@test.com";

    @BeforeAll
    void setupAll() {
        internDocumentDAO = new InternDocumentDAO();
        testDocument = new InternDocumentDTO();

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        testDocument.setOriginalName("Horario_Clases.pdf");
        testDocument.setSavedName("DOC_" + uniqueSuffix + ".pdf");
        testDocument.setFilePath("/docs/interns/DOC_" + uniqueSuffix + ".pdf");
        testDocument.setSizeMb(1.2);
        testDocument.setExtension(".pdf");
        testDocument.setUploadDate(LocalDateTime.now().withNano(0));
        testDocument.setDocumentType("CLASS_SCHEDULE");
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Guardar un documento con matrícula inexistente debe devolver false")
    void testSaveDocumentInvalidStudent() throws DAOException {
        boolean result = internDocumentDAO.saveDocument("S00000000", testDocument);
        assertFalse(result, "No se debe registrar el documento si la matrícula no existe.");
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Las consultas de verificación (has...) no deben lanzar excepciones con un correo válido")
    void testCheckMethodsValidEmailDoNotThrow() {
        assertDoesNotThrow(() -> {
            internDocumentDAO.hasClassScheduleByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasActivitiesPlanByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasPSPByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasPartialReportByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasMonthlyReportByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasSelfEvaluationByInternEmail(VALID_STUDENT_EMAIL);
            internDocumentDAO.hasEvaluationLinkedOrganizationByInternEmail(VALID_STUDENT_EMAIL);
        }, "Ningún método de consulta debe arrojar DAOException con un correo válido.");
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Las consultas de verificación deben devolver false con un correo inexistente")
    void testCheckMethodsInvalidEmailReturnFalse() throws DAOException {
        String invalidEmail = "correo.fantasma@test.com";

        assertFalse(internDocumentDAO.hasClassScheduleByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasActivitiesPlanByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasPSPByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasPartialReportByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasMonthlyReportByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasSelfEvaluationByInternEmail(invalidEmail));
        assertFalse(internDocumentDAO.hasEvaluationLinkedOrganizationByInternEmail(invalidEmail));
    }
}