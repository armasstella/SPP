package spp.businesslogic.interfaces;


import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la generación de reportes basados en indicadores.
 * Proporciona operaciones para obtener estadísticas y métricas filtradas según criterios específicos.
 */
public interface IIndicatorReportDAO {

    /**
     * Obtiene estadísticas e indicadores reportados según criterios de filtro especificados.
     *
     * Propósito: Recuperar un reporte consolidado que contenga métricas, estadísticas y KPIs (indicadores clave de desempeño)
     * calculados en base a los filtros proporcionados (por ejemplo, período, categoría, departamento), para su visualización
     * en dashboards o generación de informes ejecutivos.
     *
     * @param filters IndicatorFilterDTO que contiene los criterios de filtrado (fechas, categorías, tipos de indicador, etc.). No debe ser null.
     * @return IndicatorReportDTO con las estadísticas e indicadores resultantes del filtrado; puede contener múltiples métricas agregadas.
     * @throws DAOException Si ocurre un error al ejecutar las consultas o al mapear los resultados desde la capa de persistencia.
     */
    IndicatorReportDTO getStaticsByIndicators(IndicatorFilterDTO filters) throws DAOException;

}
