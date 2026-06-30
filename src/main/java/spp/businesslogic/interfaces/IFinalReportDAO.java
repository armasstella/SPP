package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la recuperación de informes finales de practicantes.
 * Proporciona operaciones para obtener los archivos de informe final y verificar su existencia.
 */
public interface IFinalReportDAO {

    /**
     * Recupera los archivos del informe final asociados a un practicante identificado por su número de matrícula.
     *
     * Propósito: Obtener una lista de ReportDocumentFileDTO que representan los documentos (archivos)
     * del informe final subidos por el estudiante, permitiendo su descarga, visualización, evaluación y almacenamiento.
     *
     * @param studentNumber Número de matrícula o identificador único del estudiante cuyos informes finales se desean recuperar. No debe ser null ni vacío.
     * @return Lista de ReportDocumentFileDTO que contienen metadatos y referencias a los archivos del informe final; devuelve una lista vacía si no hay informes.
     * @throws DAOException Si ocurre un error al acceder a la capa de persistencia o al mapear los resultados.
     */
    List<ReportDocumentFileDTO> getFinalReportsByIntern(String studentNumber) throws DAOException;

    /**
     * Verifica si un practicante identificado por su correo electrónico ha presentado un informe final.
     *
     * Propósito: Comprobar la existencia de un informe final registrado, permitiendo validaciones
     * sobre completitud de documentación antes de procesar una práctica como finalizada.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe un informe final registrado para ese correo; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta en la capa de datos.
     */
    boolean hasFinalReportByInternEmail(String email) throws DAOException;

}
