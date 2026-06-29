package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndicatorReportDAOTest {

    private IndicatorReportDAO indicatorReportDAO;

    @BeforeAll
    void setupAll() {
        indicatorReportDAO = new IndicatorReportDAO();
    }

    @Test
    @DisplayName("Flujo Normal: Obtener estadísticas con filtros vacíos (Todo el sistema)")
    void testGetStaticsEmptyFilters() throws DAOException {
        IndicatorFilterDTO filters = new IndicatorFilterDTO();

        IndicatorReportDTO report = indicatorReportDAO.getStaticsByIndicators(filters);

        assertNotNull(report, "El reporte no debe ser nulo.");
        assertTrue(report.getTotalStudents() >= 0);
        assertTrue(report.getTotalMale() >= 0);
        assertTrue(report.getTotalFemale() >= 0);
    }

    @Test
    @DisplayName("Flujo Normal: Obtener estadísticas con filtro de periodo activo")
    void testGetStaticsWithTermFilter() throws DAOException {
        IndicatorFilterDTO filters = new IndicatorFilterDTO();
        filters.setPeriod("FEBRERO - JULIO 26") ;

        IndicatorReportDTO report = indicatorReportDAO.getStaticsByIndicators(filters);

        assertNotNull(report);
        assertNotNull(report.getAppliedFilters());
        assertEquals("FEBRERO - JULIO 26", report.getAppliedFilters().getPeriod());
    }
}