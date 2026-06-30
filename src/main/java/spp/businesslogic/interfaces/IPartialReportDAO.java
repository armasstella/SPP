package spp.businesslogic.interfaces;

import spp.businesslogic.dto.PartialReportDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la recuperación de informes parciales (partial reports) de practicantes.
 * Proporciona operaciones para obtener los archivos de informe parcial asociados a un practicante.
 */
public interface IPartialReportDAO {

    /**
     * Obtiene el encabezado o información principal del informe parcial de un practicante identificado por su matrícula.
     *
     * Propósito: Recuperar los metadatos o resumen del informe parcial (fecha, estado, puntuaciones, observaciones preliminares)
     * permitiendo su visualización antes de acceder a los archivos completos, facilitando revisiones rápidas.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuyo informe parcial se desea obtener. No debe ser null ni vacío.
     * @return PartialReportDTO con el encabezado e información principal del informe parcial; puede devolver null si no existe registro.
     * @throws DAOException Si ocurre un error al consultar la persistencia o mapear los resultados.
     */
    PartialReportDTO findReportHeaderByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Recupera los archivos de informe parcial asociados a un practicante identificado por su número de matrícula.
     *
     * Propósito: Obtener una lista de ReportDocumentFileDTO que representan los documentos (archivos)
     * del informe parcial subidos por el practicante, para su descarga, visualización o evaluación.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuyos informes parciales se desean recuperar. No debe ser null ni vacío.
     * @return Lista de ReportDocumentFileDTO que contienen metadatos y referencias a los archivos del informe parcial; devuelve una lista vacía si no hay informes.
     * @throws DAOException Si ocurre un error al acceder a la capa de persistencia o al mapear los resultados.
     */
    List<ReportDocumentFileDTO> getPartialReportsByIntern(String studentNumber) throws DAOException;
}

