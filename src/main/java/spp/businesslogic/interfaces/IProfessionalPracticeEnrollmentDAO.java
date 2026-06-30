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
    boolean isPracticeCompletedByInternEmail(String email) throws DAOException;
    InternEnrollmentConcludeDTO getEnrollmentConcludeDatayByInternEmail(String email) throws DAOException;
    boolean assignFinalGrade(int internId, String studentNumber, int finalGrade) throws DAOException;

}
