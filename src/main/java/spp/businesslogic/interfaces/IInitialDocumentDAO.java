package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternDocumentDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para gestión de documentos iniciales y evidencias de practicantes.
 * Define operaciones para guardar documentos, comprobar la existencia de distintos tipos
 * de evidencias asociadas a un practicante y asignar calificaciones a documentos.
 */
public interface IInitialDocumentDAO {

    /**
     * Guarda un documento inicial (por ejemplo, horario, plan de actividades, PSP, reportes) asociado a un practicante.
     *
     * Propósito: Persistir el documento enviado por el practicante, junto con metadatos relevantes (tipo, fecha,
     * autor), para su posterior revisión y vinculación con la inscripción.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante al que pertenece el documento. No debe ser null ni vacío.
     * @param internDocumentDTO DTO que contiene los datos del documento a guardar (tipo, contenido o referencia, fecha, metadatos). No debe ser null.
     * @return true si el documento se guardó correctamente; false si no se pudo almacenar por reglas de negocio o validaciones.
     * @throws DAOException Si ocurre un error en la capa de persistencia (conexión, SQL, mapeo) durante la operación de guardado.
     */
    boolean saveDocument(String studentNumber, InternDocumentDTO internDocumentDTO) throws DAOException;

    /**
     * Verifica si el practicante identificado por su correo ha subido su horario de clases.
     *
     * Propósito: Determinar la existencia del horario requerido como parte de la documentación inicial
     * para la práctica, permitiendo habilitar o bloquear pasos posteriores del flujo.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe un horario de clases registrado para ese correo; false en caso contrario.
     * @throws DAOException Si ocurre un error al consultar la capa de datos.
     */
    boolean hasClassScheduleByInternEmail(String email) throws DAOException;

    /**
     * Comprueba si el practicante ha presentado un plan de actividades.
     *
     * Propósito: Verificar la existencia del documento que describe las actividades a realizar durante la práctica.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si hay un plan de actividades registrado; false si no existe.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    boolean hasActivitiesPlanByInternEmail(String email) throws DAOException;

    /**
     * Comprueba si el practicante ha subido su PSP (Plan de Servicio Profesional o documento equivalente).
     *
     * Propósito: Asegurar que el documento PSP esté presente antes de continuar con procesos que lo requieran.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe PSP registrado para ese correo; false si no existe.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    boolean hasPSPByInternEmail(String email) throws DAOException;

    /**
     * Verifica la existencia de un informe parcial para el practicante.
     *
     * Propósito: Determinar si el practicante ya ha enviado su informe parcial, documento útil para seguimiento intermedio.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe un informe parcial; false si no existe registro.
     * @throws DAOException Si ocurre un error en la capa de persistencia.
     */
    boolean hasPartialReportByInternEmail(String email) throws DAOException;

    /**
     * Verifica si existe un informe mensual cargado por el practicante.
     *
     * Propósito: Comprobar la presencia de reportes periódicos mensuales requeridos por la metodología de seguimiento.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si hay un informe mensual registrado; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta en la capa de datos.

     */
    boolean hasMonthlyReportByInternEmail(String email) throws DAOException;

    /**
     * Comprueba si el practicante ha presentado la autoevaluación.
     *
     * Propósito: Verificar la existencia del documento de autoevaluación que el practicante debe completar como parte de la evaluación final.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe una autoevaluación registrada; false si no existe.

     * @throws DAOException Si ocurre un error al acceder a la persistencia.
     */
    boolean hasSelfEvaluationByInternEmail(String email) throws DAOException;

    /**
     * Verifica si el practicante tiene registrada la evaluación realizada por la organización vinculada.
     *
     * Propósito: Comprobar la existencia de la evaluación externa (por la organización anfitriona) asociada al practicante,
     * la cual suele ser requisito para la aprobación final.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe la evaluación de la organización vinculada; false si no existe.
     * @throws DAOException Si ocurre un error en la capa de datos durante la consulta.
     */
    boolean hasEvaluationLinkedOrganizationByInternEmail(String email) throws DAOException;

    /**
     * Asigna una calificación y comentarios a un documento inicial.
     *
     * Propósito: Registrar la calificación y observaciones de un evaluador sobre un documento (por ejemplo, informe o evidencia)
     * asociado a un practicante, preservando trazabilidad y permitiendo posteriores actualizaciones.
     *
     * @param documentId Identificador numérico del documento a actualizar. Debe corresponder a un documento existente.
     * @param grade Valor numérico de la calificación asignada (por ejemplo, 0-10), respetando el rango definido en la lógica de negocio.
     * @param comments Observaciones o comentarios del evaluador que acompañan la calificación. Puede ser null o estar vacío si no hay comentarios.
     * @return true si la asignación de la calificación y comentarios fue exitosa; false si no se pudo completar por restricciones de negocio.
     * @throws DAOException Si ocurre un error en la capa de persistencia al guardar o actualizar la calificación.
     */
    boolean assignGrade(int documentId, int grade, String comments) throws DAOException;

    /**
     * Recupera los documentos asociados a una inscripción ya concluida, identificada por el correo del practicante.
     *
     * Propósito: Obtener todos los documentos presentados por un practicante cuya inscripción ya ha sido finalizada,
     * permitiendo generar un archivo histórico, reportes finales o auditorías de los entregables de la práctica.
     *
     * @param email Correo electrónico del practicante cuya inscripción concluida se asocia. No debe ser null ni vacío.
     * @return Lista de InternDocumentDTO con todos los documentos vinculados a la inscripción concluida; devuelve una lista vacía si no hay documentos.
     * @throws DAOException Si ocurre un error al acceder a la persistencia o al mapear los resultados.
     */
    List<InternDocumentDTO> getDocumentsByConcludedEnrollment(String email) throws DAOException;

    /**
     * Recupera los documentos del practicante junto con su estado de evaluación.
     *
     * Propósito: Obtener la lista de documentos asociados a un practicante, incluyendo
     * la información necesaria para conocer si cada documento ha sido evaluado,
     * calificado o permanece pendiente de revisión.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante
     *                      cuyos documentos se desean consultar. No debe ser null ni vacío.
     * @return Lista de InternDocumentReviewDTO con los documentos y su estado de evaluación;
     *         devuelve una lista vacía si el practicante no tiene documentos registrados.
     * @throws DAOException Si ocurre un error al consultar la persistencia o mapear los resultados.
     */
    List<InternDocumentReviewDTO> findDocumentsWithEvaluationStatusByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Verifica si el practicante ha entregado su carta de liberación.
     *
     * Propósito: Comprobar la existencia de la carta de liberación emitida al concluir
     * las prácticas profesionales, requisito para finalizar el proceso administrativo.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe una carta de liberación registrada; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de persistencia.
     */
    boolean hasReleaseLetterByInternEmail(String email) throws DAOException;

    /**
     * Verifica si el practicante ha entregado todos los informes mensuales requeridos.
     *
     * Propósito: Comprobar que se encuentren registrados todos los reportes mensuales
     * establecidos por la normativa de prácticas profesionales antes de permitir
     * avanzar a etapas posteriores del proceso.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si todos los informes mensuales requeridos están registrados; false en caso contrario.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    boolean hasAllMonthlyReports(String email) throws DAOException;

    /**
     * Verifica si el practicante ha entregado el informe parcial.
     *
     * Propósito: Comprobar la existencia del informe parcial requerido como evidencia
     * del avance de las prácticas profesionales.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe un informe parcial registrado; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    boolean hasPartialReport(String email) throws DAOException;

    /**
     * Verifica si el practicante ha entregado el informe final.
     *
     * Propósito: Comprobar la existencia del informe final presentado por el practicante
     * como parte del cierre del proceso de prácticas profesionales.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existe un informe final registrado; false en caso contrario.
     * @throws DAOException Si ocurre un error al acceder a la capa de persistencia.
     */
    boolean hasFinalReport(String email) throws DAOException;


}

