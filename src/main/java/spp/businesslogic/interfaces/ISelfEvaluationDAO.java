package spp.businesslogic.interfaces;

import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la recuperación de encabezados de autoevaluación de practicantes.
 * Proporciona operaciones para obtener la información principal de la autoevaluación asociada a un practicante.
 */
public interface ISelfEvaluationDAO {

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

}
