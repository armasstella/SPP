package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para operaciones relacionadas con reportes y calificaciones.
 * Contiene métodos para recuperar el detalle de un reporte por practicante y
 * para asignar y actualizar calificaciones sobre documentos asociados.
 */
public interface IReportDAO {

    /**
     * Obtiene el detalle del reporte asociado a un practicante identificado por su número de matrícula.
     *
     * Propósito: Recuperar toda la información necesaria del reporte (datos del documento, calificaciones,
     * observaciones y metadatos) correspondiente al practicante, para su visualización o procesamiento
     * en la capa de presentación.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuyo reporte se desea obtener. No debe ser null ni vacío.
     * @return ReportDTO con el detalle del reporte del practicante; puede devolver null si no existe un reporte asociado.
     * @throws DAOException Si ocurre un error en la capa de persistencia al ejecutar la consulta o mapear los resultados.
     */
    ReportDTO getReportDetailByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Asigna una calificación a un documento específico en nombre de un evaluador identificado por correo.
     *
     * Propósito: Registrar la calificación inicial de un documento (por ejemplo, informe o evidencia) realizada por un evaluador,
     * vinculando la nota con el id del documento y la identidad del evaluador (email) para trazabilidad.
     *
     * @param documentId Identificador numérico del documento que se va a calificar. Debe corresponder a un documento existente.
     * @param email Correo electrónico del evaluador que asigna la calificación; sirve para auditoría y autorización.
     * @param grade Valor numérico de la calificación asignada. Debe cumplir con las reglas de negocio (rango permitido) según la implementación.
     * @return true si la asignación de la calificación fue exitosa; false si la operación no se pudo completar por reglas de negocio.
     * @throws DAOException Si ocurre un error en la capa de persistencia al insertar o actualizar la calificación.
     */
    boolean assignGrade(int documentId, String email, int grade) throws DAOException;

    /**
     * Actualiza la calificación de un documento ya calificado.
     *
     * Propósito: Modificar la nota previamente asignada a un documento (por ejemplo, para corregir errores o aplicar validaciones posteriores),
     * preservando la trazabilidad y las reglas de negocio sobre cambios de calificación.
     *
     * @param documentId Identificador numérico del documento cuya calificación se actualizará.
     * @param grade Nuevo valor numérico de la calificación. Debe respetar el rango y reglas de negocio definidas.
     * @return true si la actualización de la calificación fue exitosa; false si el documento no existe o la operación incumple restricciones.
     * @throws DAOException Si ocurre un error durante la operación de actualización en la capa de persistencia.
     */
    boolean updateGrade(int documentId, int grade) throws DAOException;

}
