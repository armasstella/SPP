package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternEnrollmentConcludeDTO;
import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la gestión de inscripciones a la práctica profesional.
 * Contiene operaciones para registrar inscripciones y asignar proyectos o cursos a practicantes inscritos.
 */
public interface IProfessionalPracticeEnrollmentDAO {

    /**
     * Registra una inscripción a la práctica profesional.
     *
     * Propósito: Persistir la información de la inscripción de un practicante a la práctica profesional,
     * incluyendo datos del practicante, la organización vinculada, fechas y estado de la inscripción.
     *
     * @param professionalPracticeEnrollmentDTO DTO que contiene los datos de la inscripción (número de practicante,
     *                                          organización, fechas, estado, etc.). No debe ser null y debe contener
     *                                          los campos requeridos por la lógica de negocio.
     * @return true si la inscripción se registró correctamente; false si no se pudo registrar por reglas de negocio
     * (por ejemplo, inscripción duplicada o validación fallida).
     * @throws DAOException Si ocurre un error en la capa de persistencia (conexión, restricciones, errores SQL)
     * durante la operación.
     */
    boolean registerProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO professionalPracticeEnrollmentDTO)
            throws DAOException;

    /**
     * Asigna un proyecto a un practicante ya inscrito, identificado por su matrícula.
     *
     * Propósito: Vincular un proyecto (por su id) a la inscripción del practicante para que quede registrado
     * como su proyecto de práctica, permitiendo seguimiento y evaluación.
     *
     * @param studentNumber Matrícula o identificador único del practicante al que se le asignará el proyecto. No debe ser null ni vacío.
     * @param idProject Identificador numérico del proyecto que se asignará al practicante. Debe corresponder a un proyecto existente.
     * @return true si la asignación se realizó correctamente; false si la inscripción no existe o la asignación viola reglas de negocio.
     * @throws DAOException Si ocurre un error durante la actualización en la capa de datos.
     */
    boolean assignProjectByStudentNumber(String studentNumber, int idProject) throws DAOException;

    /**
     * Asigna un curso a un practicante ya inscrito, identificado por su matrícula.
     *
     * Propósito: Asociar un curso (por su id) a la inscripción del practicante, por ejemplo para registrar la asignación
     * curricular de la práctica o la relación con una materia específica.
     *
     * @param studentNumber Matrícula o identificador único del practicante al que se le asignará el curso. No debe ser null ni vacío.
     * @param courseId Identificador numérico del curso que se asignará al practicante. Debe corresponder a un curso existente en el término activo.
     * @return true si la asignación del curso fue exitosa; false si no existe la inscripción o la operación incumple alguna regla de negocio.
     * @throws DAOException Si ocurre un error en la capa de persistencia al ejecutar la actualización.
     */
    boolean assignCourseByStudentNumber(String studentNumber, int courseId) throws DAOException;

    /**
     * Verifica si la práctica profesional de un practicante identificado por su correo ha sido completada.
     *
     * Propósito: Determinar si el practicante ha finalizó exitosamente todos los requisitos de la práctica profesional,
     * incluyendo entrega de documentos, calificaciones y cumplimiento de todas las fases.
     *
     * @param email Correo electrónico del practicante a verificar. No debe ser null ni vacío.
     * @return true si la práctica está completada; false si aún está en proceso o no cumple los requisitos.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    boolean isPracticeCompletedByInternEmail(String email) throws DAOException;

    /**
     * Obtiene los datos necesarios para concluir o finalizar la inscripción de un practicante identificado por su correo.
     *
     * Propósito: Recuperar toda la información requerida para cerrar o concluir una inscripción (calificación final,
     * fecha de cierre, observaciones, estado de documentación, etc.), permitiendo procesar la liberación del practicante.
     *
     * @param email Correo electrónico del practicante cuya información de conclusión se desea obtener. No debe ser null ni vacío.
     * @return InternEnrollmentConcludeDTO con los datos para la conclusión de la inscripción; puede devolver null si la inscripción no existe.
     * @throws DAOException Si ocurre un error al acceder a la persistencia o mapear los resultados.
     */
    InternEnrollmentConcludeDTO getEnrollmentConcludeDatayByInternEmail(String email) throws DAOException;

    /**
     * Asigna la calificación final a una inscripción de práctica identificada por el id del practicante y su matrícula.
     *
     * Propósito: Registrar la nota final que el practicante obtiene al completar su práctica profesional,
     * consolidando todas las evaluaciones previas en una calificación definitiva para el cierre de la inscripción.
     *
     * @param internId Identificador numérico del practicante cuya calificación final se asignará.
     * @param studentNumber Matrícula o identificador único del practicante (para validación y trazabilidad). No debe ser null ni vacío.
     * @param finalGrade Valor numérico de la calificación final (respeta el rango definido por la lógica de negocio, por ejemplo 0-10 o 0-100).
     * @return true si la asignación de la calificación final fue exitosa; false si no se pudo completar por restricciones.
     * @throws DAOException Si ocurre un error en la capa de persistencia al guardar o actualizar la calificación final.
     */
    boolean assignFinalGrade(int internId, String studentNumber, int finalGrade) throws DAOException;

}
