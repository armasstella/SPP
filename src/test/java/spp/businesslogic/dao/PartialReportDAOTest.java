package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartialReportDAOTest {

    private PartialReportDAO partialReportDAO;

    @BeforeAll
    void setupAll() {
        partialReportDAO = new PartialReportDAO();
    }

    @Test
    @DisplayName("Flujo Normal: Obtener lista de reportes parciales y validar igualdad")
    void testGetPartialReportsByInternSuccess() throws DAOException {
        String VALID_STUDENT_NUMBER = "S22513740";
        List<ReportDocumentFileDTO> reports = partialReportDAO.getPartialReportsByIntern(VALID_STUDENT_NUMBER);

        assertNotNull(reports);

        if (!reports.isEmpty()) {
            ReportDocumentFileDTO firstReport = reports.get(0);

            assertNotNull(firstReport.getStoredName());
            assertNotNull(firstReport.getFilePath());
            assertTrue(firstReport.getDocumentId() > 0);

            if (firstReport.isGraded()) {
                assertTrue(firstReport.getGrade() >= 0 && firstReport.getGrade() <= 10);
            }
        }
    }

    @Test
    @DisplayName("Flujo Alterno: Retornar lista vacía para matrícula sin reportes")
    void testGetPartialReportsEmptyList() throws DAOException {
        List<ReportDocumentFileDTO> reports = partialReportDAO.getPartialReportsByIntern("S00000000");

        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }
}