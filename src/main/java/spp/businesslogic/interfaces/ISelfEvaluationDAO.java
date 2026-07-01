package spp.businesslogic.interfaces;

import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la recuperación de encabezados de autoevaluación de practicantes.
 * Proporciona operaciones para obtener la información principal de la autoevaluación asociada a un practicante.
 */
public interface ISelfEvaluationDAO {

    /**
     * Vincula la realización de la autoevaluación para el practicante identificado por su correo electrónico.
     *
     * Propósito: Registrar la autoevaluación realizada por el practicante,
     * permitiendo que posteriormente pueda ser buscado el registro realizado
     *
     * @param email Correo electrónico institucional del practicante para el que se creará
     *              la autoevaluación. No debe ser null ni vacío.
     * @return true si la autoevaluación se creó correctamente; false si no fue posible crearla.
     * @throws DAOException Si ocurre un error durante la operación de persistencia.
     */
    boolean saveSelfEvaluation(String email) throws DAOException;
    /**
     * Obtiene el encabezado o información principal de la autoevaluación de un practicante identificado por su matrícula.
     *
     * Propósito: Recuperar los metadatos o resumen de la autoevaluación (fecha, estado, puntuaciones resumidas, observaciones)
     * para su visualización o procesamiento antes de acceder al contenido completo.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuya autoevaluación se desea recuperar. No debe ser null ni vacío.
     * @return SelfEvaluationDTO que contiene los datos del encabezado de la autoevaluación; puede devolver null si no existe registro.
     * @throws DAOException Si ocurre un error al consultar la persistencia o mapear los resultados.
     */
    SelfEvaluationDTO findEvaluationHeaderByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Verifica si el practicante ha realizado y registrado su autoevaluación.
     *
     * Propósito: Comprobar la existencia de una autoevaluación asociada al practicante,
     * permitiendo determinar si este requisito del proceso de prácticas profesionales
     * ya fue cumplido.
     *
     * @param email Correo electrónico institucional del practicante a consultar.
     *              No debe ser null ni vacío.
     * @return true si existe una autoevaluación registrada para el practicante;
     *         false en caso contrario.
     * @throws DAOException Si ocurre un error al consultar la capa de persistencia.
     */
    boolean hasSelfEvaluation(String email) throws DAOException;

}
