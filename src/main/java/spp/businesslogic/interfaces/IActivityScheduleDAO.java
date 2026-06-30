package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la gestión de calendarios o cronogramas de actividades asociados a proyectos.
 * Proporciona operaciones para guardar programaciones de actividades vinculadas a un proyecto.
 */
public interface IActivityScheduleDAO {

    /**
     * Guarda un cronograma o calendario de actividades asociado a un proyecto específico.
     *
     * Propósito: Persistir la programación detallada de actividades (con fechas, hitos, entregables)
     * que debe completar un practicante dentro de un proyecto, facilitando el seguimiento y validación
     * del cumplimiento del plan.
     *
     * @param activityScheduleDTO DTO que contiene los datos del cronograma de actividades (descripción, fechas, hitos, prioridad, etc.). No debe ser null.
     * @param projectId Identificador numérico del proyecto al que pertenece este cronograma de actividades. Debe corresponder a un proyecto existente.
     * @return true si el cronograma se guardó correctamente; false si la operación falló por reglas de negocio (p. ej. proyecto inexistente o validaciones).
     * @throws DAOException Si ocurre un error en la capa de persistencia (errores de conexión, SQL, mapeo) durante el guardado.
     */
    boolean saveActivitySchedule(ActivityScheduleDTO activityScheduleDTO, int projectId) throws DAOException;
}
